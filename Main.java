import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class DonationCampaign {
    private String cause;
    private double targetAmount;
    private double currentAmount;
    private String deadline;
    private List<Donor> donors;

    public DonationCampaign(String cause, double targetAmount, String deadline) {
        this.cause = cause;
        this.targetAmount = targetAmount;
        this.deadline = deadline;
        this.currentAmount = 0;
        this.donors = new ArrayList<>();
    }

    public String getCause() {
        return cause;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public String getDeadline() {
        return deadline;
    }

    public List<Donor> getDonors() {
        return donors;
    }

    public void donate(String donorName, double amount) {
        Donor donor = new Donor(donorName, amount);
        donors.add(donor);
        currentAmount += amount;
    }

    @Override
    public String toString() {
        return "Campaign: " + cause +
                "\nTarget Amount: Rs" + targetAmount +
                "\nCurrent Amount: Rs" + currentAmount +
                "\nDeadline: " + deadline +
                "\nDonors: " + donors.size();
    }
}

class Donor {
    private String donorName;
    private double donatedAmount;

    public Donor(String donorName, double donatedAmount) {
        this.donorName = donorName;
        this.donatedAmount = donatedAmount;
    }

    public String getDonorName() {
        return donorName;
    }

    public double getDonatedAmount() {
        return donatedAmount;
    }

    @Override
    public String toString() {
        return "Donor: " + donorName + ", Amount: Rs" + donatedAmount;
    }
}

class DonationManagementSystemGUI extends JFrame {
    private DonationManagementSystem donationSystem;

    private JTextArea campaignsTextArea;
    private JTextField causeField;
    private JTextField donorNameField;
    private JTextField amountField;

    public DonationManagementSystemGUI() {
        super("Donation Management System");

        donationSystem = new DonationManagementSystem();

        campaignsTextArea = new JTextArea(10, 30);
        campaignsTextArea.setEditable(false);

        JLabel causeLabel = new JLabel("Campaign Cause:");
        JLabel donorNameLabel = new JLabel("Your Name:");
        JLabel amountLabel = new JLabel("Donation Amount (Rs):");

        causeField = new JTextField(20);
        donorNameField = new JTextField(20);
        amountField = new JTextField(10);

        JButton viewCampaignsButton = new JButton("View Campaigns");
        JButton donateButton = new JButton("Donate");
        JButton viewDonorsButton = new JButton("View Donors");

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(new JScrollPane(campaignsTextArea));
        add(causeLabel);
        add(causeField);
        add(donorNameLabel);
        add(donorNameField);
        add(amountLabel);
        add(amountField);
        add(viewCampaignsButton);
        add(donateButton);
        add(viewDonorsButton);

        viewCampaignsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCampaigns();
            }
        });

        donateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                donateToCampaign();
            }
        });

        viewDonorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewDonors();
            }
        });
    }

    private void displayCampaigns() {
        campaignsTextArea.setText("");
        for (DonationCampaign campaign : donationSystem.getCampaigns()) {
            campaignsTextArea.append(campaign + "\n\n");
        }
    }

    private void donateToCampaign() {
        String cause = causeField.getText();
        String donorName = donorNameField.getText();
        String amountText = amountField.getText();

        if (cause.isEmpty() || donorName.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            donationSystem.donateToCampaign(cause, donorName, amount);
            JOptionPane.showMessageDialog(this, "Donation successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Clear input fields after donation
        causeField.setText("");
        donorNameField.setText("");
        amountField.setText("");
    }

    private void viewDonors() {
        StringBuilder donorsText = new StringBuilder("Donors:\n");
        for (DonationCampaign campaign : donationSystem.getCampaigns()) {
            donorsText.append("Campaign: ").append(campaign.getCause()).append("\n");
            for (Donor donor : campaign.getDonors()) {
                donorsText.append(donor).append("\n");
            }
            donorsText.append("\n");
        }

        JTextArea donorsTextArea = new JTextArea(donorsText.toString(), 15, 30);
        donorsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(donorsTextArea);

        JOptionPane.showMessageDialog(this, scrollPane, "Donors List", JOptionPane.PLAIN_MESSAGE);
    }
}

class DonationManagementSystem {
    private List<DonationCampaign> campaigns;

    public DonationManagementSystem() {
        this.campaigns = new ArrayList<>();
    }

    public List<DonationCampaign> getCampaigns() {
        return campaigns;
    }

    public void donateToCampaign(String cause, String donorName, double amount) {
        for (DonationCampaign campaign : campaigns) {
            if (campaign.getCause().equalsIgnoreCase(cause)) {
                campaign.donate(donorName, amount);
                return;
            }
        }

        // If the cause is not found, add a new campaign
        DonationCampaign newCampaign = new DonationCampaign(cause, amount * 5, "2025-01-01");
        newCampaign.donate(donorName, amount);
        campaigns.add(newCampaign);
    }
}

public class Main{
    public static void main(String[] args) {
        DonationManagementSystemGUI gui = new DonationManagementSystemGUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(400, 600);
        gui.setVisible(true);
    }
}
