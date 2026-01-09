package com.franquicias.infrastructure.persistence.r2dbc.repository;

import com.franquicias.domain.model.Producto;
import com.franquicias.domain.model.ProductoMaximoStock;
import com.franquicias.domain.repository.ProductoRepository;
import com.franquicias.infrastructure.persistence.r2dbc.entity.ProductoEntity;
import com.franquicias.infrastructure.persistence.r2dbc.mapper.ProductoMapper;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

interface ProductoSpringDataRepository extends R2dbcRepository<ProductoEntity, Long> {
    Flux<ProductoEntity> findBySucursalId(Long sucursalId);
}

@Repository
public class ProductoR2dbcRepository implements ProductoRepository {
    
    private final ProductoSpringDataRepository springDataRepository;
    private final DatabaseClient databaseClient;

    public ProductoR2dbcRepository(ProductoSpringDataRepository springDataRepository,
                                   DatabaseClient databaseClient) {
        this.springDataRepository = springDataRepository;
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        ProductoEntity entity = ProductoMapper.toEntity(producto);
        LocalDateTime now = LocalDateTime.now();
        
        if (entity.getId() == null) {
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            return springDataRepository.save(entity)
                    .map(ProductoMapper::toDomain);
        } else {
            entity.setUpdatedAt(now);
            return databaseClient.sql("UPDATE producto SET nombre = :nombre, stock = :stock, updated_at = :updatedAt WHERE id = :id")
                    .bind("nombre", entity.getNombre())
                    .bind("stock", entity.getStock())
                    .bind("updatedAt", entity.getUpdatedAt())
                    .bind("id", entity.getId())
                    .fetch()
                    .rowsUpdated()
                    .then(findById(entity.getId()));
        }
    }

    @Override
    public Mono<Producto> findById(Long id) {
        return springDataRepository.findById(id)
                .map(ProductoMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return springDataRepository.existsById(id);
    }

    @Override
    public Flux<Producto> findBySucursalId(Long sucursalId) {
        return springDataRepository.findBySucursalId(sucursalId)
                .map(ProductoMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return springDataRepository.deleteById(id)
                .then();
    }

    @Override
    public Mono<Producto> update(Producto producto) {
        return save(producto);
    }

    @Override
    public Flux<ProductoMaximoStock> findProductosMaximoStockByFranquiciaId(Long franquiciaId) {
        String sql = """
            SELECT p.id AS producto_id, p.nombre AS producto_nombre, p.stock, 
                   s.id AS sucursal_id, s.nombre AS sucursal_nombre
            FROM producto p
            INNER JOIN sucursal s ON p.sucursal_id = s.id
            INNER JOIN (
                SELECT sucursal_id, MAX(stock) AS max_stock
                FROM producto
                WHERE sucursal_id IN (SELECT id FROM sucursal WHERE franquicia_id = :franquiciaId)
                GROUP BY sucursal_id
            ) AS max_stocks ON p.sucursal_id = max_stocks.sucursal_id AND p.stock = max_stocks.max_stock
            WHERE s.franquicia_id = :franquiciaId
            ORDER BY s.id, p.stock DESC
            """;
        
        return databaseClient.sql(sql)
                .bind("franquiciaId", franquiciaId)
                .map((row, metadata) -> {
                    ProductoMaximoStock productoMaximoStock = new ProductoMaximoStock();
                    productoMaximoStock.setProductoId(row.get("producto_id", Long.class));
                    productoMaximoStock.setProductoNombre(row.get("producto_nombre", String.class));
                    productoMaximoStock.setStock(row.get("stock", Integer.class));
                    productoMaximoStock.setSucursalId(row.get("sucursal_id", Long.class));
                    productoMaximoStock.setSucursalNombre(row.get("sucursal_nombre", String.class));
                    return productoMaximoStock;
                })
                .all();
    }
}

