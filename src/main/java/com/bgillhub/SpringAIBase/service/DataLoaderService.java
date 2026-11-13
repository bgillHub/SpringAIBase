package com.bgillhub.SpringAIBase.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DataLoaderService {
    private static final Logger logger = LoggerFactory.getLogger(DataLoaderService.class);

    //TODO: Find suitable rag data source..maybe a LOTR Skiing fanfic
    //Have this import on condition from an S3 location

    @Value("classpath:/data/Sample_Data.pdf")
    private Resource pdfResource;

    @Autowired
    private VectorStore vectorStore;

    public void load() {
        logger.info("LOADING LOTR SKIS");
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(this.pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfBottomTextLinesToDelete(3)
                                .withNumberOfTopPagesToSkipBeforeDelete(1)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        var tokenTextSplitter = TokenTextSplitter.builder().withKeepSeparator(true)
                .withChunkSize(10000)
                .withMinChunkSizeChars(10)
                .build();
        this.vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));
    }
}