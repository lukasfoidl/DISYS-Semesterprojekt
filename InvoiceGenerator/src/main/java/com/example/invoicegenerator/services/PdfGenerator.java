package com.example.invoicegenerator.services;

import com.example.invoicegenerator.data.StationData;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

public class PdfGenerator extends BaseService {

    public PdfGenerator(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);
    }

    @Override
    protected String executeInternal(String receiveMessage) {

        // extract values from JSON
        int customerId = new JSONObject(receiveMessage).getInt("customerId");
        JSONArray array = new JSONObject(receiveMessage).getJSONArray("stationData");
        ArrayList<StationData> stationData = new ArrayList<>();
        for (Object item : array) {
            JSONObject obj = (JSONObject) item;
            stationData.add(new StationData(
                    obj.getInt("stationId"),
                    obj.getDouble("amount"),
                    obj.getString("date"),
                    obj.getInt("kwh")
            ));
        }

        System.out.println("PdfGenerator: executeInternal(customerId " + customerId + ")");

        generatePDF(customerId, stationData);

        return null;
    }

    private void generatePDF(int customerId, ArrayList<StationData> stationData) {
        String invoiceNumber = UUID.randomUUID().toString();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("./Invoices/Invoice_" + customerId + ".pdf"));
            document.open();

            // Headline
            Chunk chunk = createHeadline(invoiceNumber);
            document.add(chunk);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

            // main text
            String text = createMainText(customerId, invoiceNumber);
            document.add(new Paragraph(text));

            // table
            PdfPTable table = createTable(stationData);
            document.add(table);

            // end statement
            document.add(new Paragraph("\n We hope to see you soon at one of our charging stations or at one of our service centers. \n Have a good and save ride."));

            document.close();
        } catch (Exception e){
            System.out.println("ERROR - " + e.getMessage());
        }
    }

    private Chunk createHeadline(String invoiceNumber) {
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, BaseColor.BLACK);
        Chunk chunk = new Chunk("INVOICE - " + invoiceNumber, font);
        return chunk;
    }

    private String createMainText(int customerId, String invoiceNumber) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String date = dtf.format(LocalDateTime.now());
        String text =
            "ECA Electrical Charging Association \n" +
            "901 4th Street NW \n" +
            "Washington, DC 20001-3719 \n" +
            "\n" +
            date + "\n" +
            "\n" +
            "Dear Sir/Madam, \n" +
            "\n" +
            "Currently, there are outstanding amounts for customer with ID " + customerId + ", the details are listed below. " +
            "Please transfer the open amount within next 14 days to the following bank details: \n" +
            "\n" +
            "SeaSpark Bank \n" +
            "IBAN: US10 2000 4787 3358 \n" +
            "BIC: SSAAAAK56 \n" +
            "Reference: " + invoiceNumber + "\n" +
            "\n";

        return text;
    }

    private PdfPTable createTable(ArrayList<StationData> stationData) {
        PdfPTable table = new PdfPTable(4);
        addTableHeader(table);

        // https://stackoverflow.com/questions/2784514/sort-arraylist-of-custom-objects-by-property
        Collections.sort(stationData, new Comparator<StationData>() {
            @Override
            public int compare(StationData o1, StationData o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        addRows(table, stationData);

        return table;
    }

    private void addTableHeader(PdfPTable table) {
        PdfPCell cell;

        cell = new PdfPCell(new Phrase("Date"));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Station ID"));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Kwh"));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Amount"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }

    private void addRows(PdfPTable table, ArrayList<StationData> stationData) {
        double sum = 0;
        PdfPCell cell;
        for (StationData item : stationData) {
            sum += item.getAmount();

            cell = new PdfPCell(new Phrase(item.getDate()));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(Integer.toString(item.getStationId())));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(Integer.toString(item.getKwh())));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(Double.toString(item.getAmount())));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
        }

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

        cell = new PdfPCell(new Phrase("TOTAL", font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(Double.toString(sum), font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }
}
