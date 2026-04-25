package com.skillagent.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class PDFService {

    public String extractText(InputStream inputStream) {
        try {
            PDDocument document = PDDocument.load(inputStream);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            document.close();

            System.out.println("Extracted PDF Text:");
            System.out.println(text);

            return text;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}