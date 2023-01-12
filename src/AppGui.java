import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AppGui extends JFrame implements ActionListener {

    CardLayout mainLayout = new CardLayout();
    CardLayout listLayout = new CardLayout();
    JPanel firstPage, secondPage, thirdPage;
    // firstPage
    JPanel dataPanel, buttonPanel1, practicalityPanel, fuelPanel, centerPanel;
    JTextField minPrice, maxPrice;
    JButton forwardButton;
    JLabel price, practicalityLabel, fuelLabel;
    JComboBox<String> practicalityCombo;
    JCheckBox petrolCheck, hybridCheck, electricCheck, dieselCheck;
    // secondPage
    JPanel buttonPanel2, dragPanel, loadingPanel;
    JButton backButton, searchButton;
    JLayeredPane layeredPane;
    JLabel loadingGif;

    public AppGui() {
        super("findcar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        setLocation(0, 0);
        setLayout(mainLayout);

        // panel 1
        firstPage = new JPanel();
        firstPage.setPreferredSize(new Dimension(1000, 600));
        firstPage.setLayout(new BoxLayout(firstPage, BoxLayout.PAGE_AXIS));

        // collecting data
        //todo
        // i oczywiście trzeba ogarnąć przypadki jak użytkownik wpisze coś innego niż liczby, albo nic nie wpisze
        dataPanel = new JPanel();
        minPrice = new JTextField("0");
        minPrice.setPreferredSize(new Dimension(90, 30));
        maxPrice = new JTextField("0");
        maxPrice.setPreferredSize(new Dimension(90, 30));
        price = new JLabel("Enter price");
        dataPanel.add(price);
        dataPanel.add(minPrice);
        dataPanel.add(maxPrice);
        dataPanel.setMaximumSize(new Dimension(300,30));
        firstPage.add(dataPanel);
        firstPage.add(Box.createRigidArea(new Dimension(0,50)));





        // combo box

        practicalityCombo = new JComboBox<>();
        practicalityCombo.addItem("Miasto");
        practicalityCombo.addItem("Trasa");
        practicalityCombo.addItem("Rodzinny");
        practicalityCombo.addItem("Uniwersalny");
        practicalityCombo.setPreferredSize(new Dimension(160,50));
        practicalityCombo.addActionListener(this);
        practicalityLabel = new JLabel("<html><center>Wybierz</center>Przeznaczenie:</html>");
        practicalityPanel = new JPanel();
        practicalityPanel.add(practicalityLabel);
        practicalityPanel.add(practicalityCombo);
        practicalityPanel.setMaximumSize(new Dimension(300,100));
        firstPage.add(practicalityPanel);
        firstPage.add(Box.createRigidArea(new Dimension(0,50)));


        // fuel panel
        fuelPanel = new JPanel();
        fuelLabel = new JLabel("Wybierz rodzaj paliwa: ");
        fuelLabel.setPreferredSize(new Dimension(135,20));
        fuelPanel.add(fuelLabel);

        petrolCheck = new JCheckBox("Benzyna",false);
        hybridCheck = new JCheckBox("Hybrydowe",false);
        electricCheck = new JCheckBox("Elektryczne",false);
        dieselCheck = new JCheckBox("Diesel",false);

        petrolCheck.setPreferredSize(new Dimension(70,20));
        hybridCheck.setPreferredSize(new Dimension(85,20));
        electricCheck.setPreferredSize(new Dimension(85,20));
        dieselCheck.setPreferredSize(new Dimension(100,20));

        petrolCheck.addActionListener(this);
        hybridCheck.addActionListener(this);
        electricCheck.addActionListener(this);
        dieselCheck.addActionListener(this);


        fuelPanel.add(petrolCheck);
        fuelPanel.add(hybridCheck);
        fuelPanel.add(electricCheck);
        fuelPanel.add(dieselCheck);
        fuelPanel.setMaximumSize(new Dimension(700,30));

        firstPage.add(fuelPanel);
        firstPage.add(Box.createVerticalGlue());

        // buttons
        forwardButton = new JButton("Next");
        forwardButton.setActionCommand("forward");
        forwardButton.setPreferredSize(new Dimension(90, 30));
        forwardButton.addActionListener(this);
        buttonPanel1 = new JPanel();
        buttonPanel1.add(forwardButton);
        buttonPanel1.setMaximumSize(new Dimension(90,30));

        firstPage.add(buttonPanel1);


        add(firstPage);
        // end panel 1

        // panel 2
        secondPage = new JPanel();
        secondPage.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        secondPage.setLayout(new BorderLayout());

        // drag and drop
        dragPanel = new DragAndDropList();
        secondPage.add(dragPanel, BorderLayout.CENTER);

        // buttons
        backButton = new JButton("Back");
        backButton.setActionCommand("back");
        backButton.setPreferredSize(new Dimension(90, 30));
        backButton.addActionListener(this);
        buttonPanel2 = new JPanel();
        buttonPanel2.add(backButton);
        searchButton = new JButton("Search");
        searchButton.setActionCommand("search");
        searchButton.setPreferredSize(new Dimension(90, 30));
        searchButton.addActionListener(this);
        buttonPanel2.add(searchButton);
        secondPage.add(buttonPanel2, BorderLayout.SOUTH);

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.add(secondPage, new Integer(0));

        loadingPanel = new JPanel();
        loadingPanel.setLayout(new BorderLayout());
        ImageIcon loading = new ImageIcon("loadgif.gif");
        loadingGif = new JLabel(loading);
        loadingPanel.add(loadingGif);
        loadingPanel.setVisible(false);
        layeredPane.add(loadingPanel, new Integer(1));
        add(layeredPane);
        // end panel2

        // panel3
        thirdPage = new JPanel();
        thirdPage.setLayout(listLayout);
        add(thirdPage);
        // end panel3

        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("forward")) {
            CarData.setMinPrice(Integer.parseInt(minPrice.getText()));
            CarData.setMaxPrice(Integer.parseInt(maxPrice.getText()));
            CarData.practicality = (String) practicalityCombo.getSelectedItem();

            System.out.println(CarData.practicality);
            System.out.println(CarData.fuel);

            CarData.filterData();
            mainLayout.next(this.getContentPane());
        }

        if (e.getActionCommand().equals("back")) {
            mainLayout.previous(this.getContentPane());
        }
        if (e.getActionCommand().equals("search")) {
            loadingPanel.setVisible(true);
            Thread listingCars = new Thread(new Runnable() {
                @Override
                public void run() {
                    thirdPage.removeAll();
                    CarData.traits = new ArrayList<>(DragAndDropList.dndList.getSelectedValuesList());
                    CarData.findCars();
                    CarList.frame = AppGui.this;
                    CarList carsList = new CarList();
                    thirdPage.add(carsList);
                    mainLayout.next(AppGui.this.getContentPane());
                    loadingPanel.setVisible(false);
                }
            });
            listingCars.start();

        }
        if (e.getSource().equals(petrolCheck)){
            if (petrolCheck.isSelected()){
                CarData.fuel.add(petrolCheck.getText());
            }else {
                CarData.fuel.remove(petrolCheck.getText());
            }

        }
        if (e.getSource().equals(hybridCheck)){
            if (hybridCheck.isSelected()){
                CarData.fuel.add(hybridCheck.getText());
            }else {
                CarData.fuel.remove(hybridCheck.getText());
            }

        }
        if (e.getSource().equals(dieselCheck)){
            if (dieselCheck.isSelected()){
                CarData.fuel.add(dieselCheck.getText());
            }else {
                CarData.fuel.remove(dieselCheck.getText());
            }

        }
        if (e.getSource().equals(electricCheck)){
            if (electricCheck.isSelected()){
                CarData.fuel.add(electricCheck.getText());
            }else {
                CarData.fuel.remove(electricCheck.getText());
            }

        }


    }

    public void showGui() {
        this.setVisible(true);
    }

    public static void main(String[] args) {
        CarData.loadData();
        FlatLightLaf.setup();
        AppGui app = new AppGui();
        app.showGui();


    }
}