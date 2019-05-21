import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class Pakbon{

    private Customer customer;
    private int y;

    public Pakbon(Customer customer) {
        this.customer = customer;
    }

    public void createPakbon(){

        y = 495;
        try{
   customer.getCustomerInfo();
            System.out.println("PDF wordt gemaakt");
            System.out.println("ordernr: " + customer.getOrder().getOrderNr());
            System.out.println("binnr: " + customer.getBin().getBinNumber());
            String fileName = (String) Integer.toString(customer.getOrder().getOrderNr()) + Integer.toString(customer.getBin().getBinNumber()) + ".pdf"; // name of our file

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();

            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);

            PDImageXObject pdImage = PDImageXObject.createFromFile("C:\\Users\\Rick\\IdeaProjects\\MagazijnRobot\\WWI.png", doc);


            //content.drawImage(pdImage, 300, 670);
            content.drawImage(pdImage, 380, 718, 167, 60);


            // Layout
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 26);
            content.moveTextPositionByAmount(80, 740);
            content.drawString("Wide World Importers");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.moveTextPositionByAmount(80, 700);
            content.drawString("Klantgegevens");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 14);
            content.moveTextPositionByAmount(80, 625);
            content.drawString("Order nummer: ");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.moveTextPositionByAmount(80, 610);
            content.drawString("klantnummer: ");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.moveTextPositionByAmount(80, 595);
            content.drawString("Bin nummer: ");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 17);
            content.moveTextPositionByAmount(80, 550);
            content.drawString("Pakbon ");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.moveTextPositionByAmount(80,525);
            content.drawString("ID / NAAM ");
            content.endText();

            content.drawLine(80, 515, 550, 515);

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.moveTextPositionByAmount(500,525);
            content.drawString("Aantal");
            content.endText();

            content.drawLine(80, 100, 550, 100);

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.moveTextPositionByAmount(80,65);
            content.drawString("Bedankt voor uw bestelling");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.moveTextPositionByAmount(80,50);
            content.drawString("Wij zien u graag weer bij een volgende bestelling!");
            content.endText();


            //Information
            //Lastname
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.moveTextPositionByAmount(80, 685);
            System.out.println("last name: " + customer.lastName);
            content.drawString(customer.lastName);
            content.endText();
            //Address
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.moveTextPositionByAmount(80, 670);
            content.drawString(customer.address);
            content.endText();
            //Postalcode & Residence
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.moveTextPositionByAmount(80, 655);
            content.drawString(customer.postalcode +" " +customer.residence);
            content.endText();
            //Ordernummer
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 15);
            content.moveTextPositionByAmount(180, 625);
            content.drawString(Integer.toString(customer.getOrder().getOrderNr()));
            content.endText();
            //UserID
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.moveTextPositionByAmount(155, 610);
            content.drawString(customer.userID);
            content.endText();
            //BinID
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.moveTextPositionByAmount(155, 595);
            content.drawString(Integer.toString(customer.getBin().getBinNumber()));
            content.endText();

            for(Article a: customer.getBin().getArticles()) {
                a.getName();
                a.getId();
                a.getAmountReserved();

                //Product
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 12);
                content.moveTextPositionByAmount(80, y);
                content.drawString(a.getId() + " " + a.getName());
                content.endText();
                //Quantity
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 12);
                content.moveTextPositionByAmount(520, y);
                content.drawString(Integer.toString(a.getAmountReserved()));
                content.endText();
                y = y -15;
            }
            content.close();
            doc.save(fileName);
            doc.close();

            System.out.println("your file created in : "+ System.getProperty("user.dir"));

        }
        catch(IOException e){

            System.out.println(e.getMessage());

        }

    }

}