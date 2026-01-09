package com.franquicias.infrastructure.persistence.r2dbc.repository;

import com.franquicias.domain.model.Sucursal;
import com.franquicias.domain.repository.SucursalRepository;
import com.franquicias.infrastructure.persistence.r2dbc.entity.SucursalEntity;
import com.franquicias.infrastructure.persistence.r2dbc.mapper.SucursalMapper;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

interface SucursalSpringDataRepository extends R2dbcRepository<SucursalEntity, Long> {
    Flux<SucursalEntity> findByFranquiciaId(Long franquiciaId);
}

@Repository
public class SucursalR2dbcRepository implements SucursalRepository {
    
    private final SucursalSpringDataRepository springDataRepository;
    private final DatabaseClient databaseClient;

    public SucursalR2dbcRepository(SucursalSpringDataRepository springDataRepository,
                                    DatabaseClient databaseClient) {
        this.springDataRepository = springDataRepository;
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Sucursal> save(Sucursal sucursal) {
        SucursalEntity entity = SucursalMapper.toEntity(sucursal);
        LocalDateTime now = LocalDateTime.now();
        
        if (entity.getId() == null) {
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            return springDataRepository.save(entity)
                    .map(SucursalMapper::toDomain);
        } else {
            entity.setUpdatedAt(now);
            return databaseClient.sql("UPDATE sucursal SET nombre = :nombre, updated_at = :updatedAt WHERE id = :id")
                    .bind("nombre", entity.getNombre())
                    .bind("updatedAt", entity.getUpdatedAt())
                    .bind("id", entity.getId())
                    .fetch()
                    .rowsUpdated()
                    .then(findById(entity.getId()));
        }
    }

    @Override
    public Mono<Sucursal> findById(Long id) {
        return springDataRepository.findById(id)
                .map(SucursalMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return springDataRepository.existsById(id);
    }

    @Override
    public Flux<Sucursal> findByFranquiciaId(Long franquiciaId) {
        return springDataRepository.findByFranquiciaId(franquiciaId)
                .map(SucursalMapper::toDomain);
    }

    @Override
    public Mono<Sucursal> update(Sucursal sucursal) {
        return save(sucursal);
    }
}

