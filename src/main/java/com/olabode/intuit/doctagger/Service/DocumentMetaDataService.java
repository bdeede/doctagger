package com.olabode.intuit.doctagger.Service;

import java.util.List;

import com.olabode.intuit.doctagger.Data.Objects.DocumentMetaData;

public interface DocumentMetaDataService {
    List<DocumentMetaData> GetAllDocumentMetaData(String documentKey);
    DocumentMetaData GetDocumentMetaData(long id);
    DocumentMetaData CreateDocumentMetaData(DocumentMetaData documentMetaData);
    Boolean DeleteDocumentMetaData(Long id);

}
