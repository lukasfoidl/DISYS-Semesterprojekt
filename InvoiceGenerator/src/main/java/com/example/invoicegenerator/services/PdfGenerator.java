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
            stationData.add(new StationData(obj.getInt("stationId"), obj.getDouble("amount")));
        }

        System.out.println("PdfGenerator: executeInternal(customerId " + customerId + ")");

        generatePDF(customerId, stationData);

        return null;
    }

    private void generatePDF(int customerId, ArrayList<StationData> stationData) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("./Invoices/Invoice_" + customerId + ".pdf"));
            document.open();

            // Headline
            Chunk chunk = createHeadline();
            document.add(chunk);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

            // main text
            String text = createMainText(customerId);
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

    private Chunk createHeadline() {
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, BaseColor.BLACK);
        Chunk chunk = new Chunk("INVOICE", font);
        return chunk;
    }

    private String createMainText(int customerId) {
        int invoiceNumber = getRandomNumber(100000, 999999);
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
            "Usage: " + invoiceNumber + "\n" +
            "\n";

        return text;
    }

    // https://stackoverflow.com/questions/5392693/java-random-number-with-given-length
    private int getRandomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }

    private PdfPTable createTable(ArrayList<StationData> stationData) {
        PdfPTable table = new PdfPTable(2);
        addTableHeader(table);
        addRows(table, stationData);

        return table;
    }

    private void addTableHeader(PdfPTable table) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Station ID"));
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

            cell = new PdfPCell(new Phrase(Integer.toString(item.getStationId())));
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

        cell = new PdfPCell(new Phrase(Double.toString(sum), font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }
}
