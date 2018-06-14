package com.olabode.intuit.doctagger.Service;

import java.util.List;

import com.olabode.intuit.doctagger.Data.Objects.MetaDataTag;

public interface MetaDataTagService {
    List<MetaDataTag> GetAllUserMetaDataTag(Long userId);
    MetaDataTag GetMetaDataTag(long id);
    List<MetaDataTag> GetTopUsedMetaDataTags(int limit);
    MetaDataTag CreateMetaDataTag(MetaDataTag metaDataTag);
    Boolean DeleteMetaDataTag(Long id);
}
