package com.franquicias.infrastructure.persistence.r2dbc.repository;

import com.franquicias.domain.model.Franquicia;
import com.franquicias.domain.repository.FranquiciaRepository;
import com.franquicias.infrastructure.persistence.r2dbc.entity.FranquiciaEntity;
import com.franquicias.infrastructure.persistence.r2dbc.mapper.FranquiciaMapper;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

interface FranquiciaSpringDataRepository extends R2dbcRepository<FranquiciaEntity, Long> {
}

@Repository
public class FranquiciaR2dbcRepository implements FranquiciaRepository {
    
    private final FranquiciaSpringDataRepository springDataRepository;
    private final DatabaseClient databaseClient;

    public FranquiciaR2dbcRepository(FranquiciaSpringDataRepository springDataRepository,
                                     DatabaseClient databaseClient) {
        this.springDataRepository = springDataRepository;
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Franquicia> save(Franquicia franquicia) {
        FranquiciaEntity entity = FranquiciaMapper.toEntity(franquicia);
        LocalDateTime now = LocalDateTime.now();
        
        if (entity.getId() == null) {
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            return springDataRepository.save(entity)
                    .map(FranquiciaMapper::toDomain);
        } else {
            entity.setUpdatedAt(now);
            return databaseClient.sql("UPDATE franquicia SET nombre = :nombre, updated_at = :updatedAt WHERE id = :id")
                    .bind("nombre", entity.getNombre())
                    .bind("updatedAt", entity.getUpdatedAt())
                    .bind("id", entity.getId())
                    .fetch()
                    .rowsUpdated()
                    .then(findById(entity.getId()));
        }
    }

    @Override
    public Mono<Franquicia> findById(Long id) {
        return springDataRepository.findById(id)
                .map(FranquiciaMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return springDataRepository.existsById(id);
    }

    @Override
    public Mono<Franquicia> update(Franquicia franquicia) {
        return save(franquicia);
    }
}

