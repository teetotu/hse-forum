package ru.hse.forum.util;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.BooleanType;

public class SqlFunctionsMetadataBuilderContributor implements MetadataBuilderContributor {
    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
        metadataBuilder.applySqlFunction("ftss",
                new SQLFunctionTemplate(BooleanType.INSTANCE,
                        "to_tsvector(post_title) @@ plainto_tsquery(?1)"));
        metadataBuilder.applySqlFunction("ftsp",
                new SQLFunctionTemplate(BooleanType.INSTANCE,
                        "to_tsvector(name) @@ plainto_tsquery(?1)"));
    }
}

