package com.olabode.intuit.doctagger.Data.Repository;

import javax.transaction.Transactional;

import com.olabode.intuit.doctagger.Data.Objects.DocumentMetaData;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;


public interface DocumentMetadataRepository extends CrudRepository<DocumentMetaData, Long> {
    @Transactional
    @Modifying
    void deleteByDocumentKey(String documentKey);
}
