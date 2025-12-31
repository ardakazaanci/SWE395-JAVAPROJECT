import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SmartDriveApp extends JFrame {

    private JTextField txtId, txtBrand, txtModel, txtYear, txtPrice, txtSearch;
    private JTextArea displayArea;
    private JButton btnRegister, btnViewAll, btnDelete, btnUpdate, btnSearch, btnClear;
    private CarDAO carDAO;

    public SmartDriveApp() {
        setTitle("Car Rental System");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        carDAO = new CarDAO();

        // --- ANA KONTEYNER ---
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. FORM ALANI
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Vehicle Information"));
        
        formPanel.add(new JLabel("ID (Operating No):"));
        txtId = new JTextField();
        txtId.setToolTipText("Only for Delete/Update");
        formPanel.add(txtId);

        formPanel.add(new JLabel("Brand:"));
        txtBrand = new JTextField();
        formPanel.add(txtBrand);

        formPanel.add(new JLabel("Model:"));
        txtModel = new JTextField();
        formPanel.add(txtModel);

        formPanel.add(new JLabel("Year:"));
        txtYear = new JTextField();
        formPanel.add(txtYear);

        formPanel.add(new JLabel("Price:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);

        // 2. BUTONLAR PANELİ
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnRegister = new JButton("Save");
        btnRegister.setBackground(new Color(220, 255, 220));
        btnRegister.setPreferredSize(new Dimension(100, 35));
        
        btnUpdate = new JButton("Update");
        btnUpdate.setPreferredSize(new Dimension(100, 35));

        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(255, 220, 220));
        btnDelete.setPreferredSize(new Dimension(100, 35));
        
        btnClear = new JButton("Clean");
        btnClear.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // 3. ARAMA PANELİ
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchPanel.setBackground(new Color(245, 245, 245));

        searchPanel.add(new JLabel("Search Brand:"));
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Search");
        btnViewAll = new JButton("List All");

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnViewAll);

        topContainer.add(formPanel);
        topContainer.add(Box.createVerticalStrut(10));
        topContainer.add(buttonPanel);
        topContainer.add(Box.createVerticalStrut(10));
        topContainer.add(searchPanel);

        add(topContainer, BorderLayout.NORTH);

        // --- LİSTE EKRANI ---
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Database Records"));
        add(scrollPane, BorderLayout.CENTER);

        // --- AKSİYONLAR ---

        // 1. KAYDETME
        btnRegister.addActionListener(e -> {
            try {
                if(txtBrand.getText().isEmpty() || txtPrice.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Brand and Price are necessary!");
                    return;
                }
                String b = txtBrand.getText();
                String m = txtModel.getText();
                int y = Integer.parseInt(txtYear.getText());
                double p = Double.parseDouble(txtPrice.getText());
                
                carDAO.addCar(new Car(b, m, y, p));
                JOptionPane.showMessageDialog(this, "Saved!");
                listele();
                temizleGiris();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Enter only numbers in the Year and Price fields.");
            }
        });

        // 2. GÜNCELLEME
        btnUpdate.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                String b = txtBrand.getText();
                String m = txtModel.getText();
                int y = Integer.parseInt(txtYear.getText());
                double p = Double.parseDouble(txtPrice.getText());
                
                carDAO.updateCar(new Car(b, m, y, p), id);
                JOptionPane.showMessageDialog(this, "Updated!");
                listele();
                temizleGiris();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: ID, Year and Price  must be a number.");
            }
        });

        // 3. SİLME
        btnDelete.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                if(JOptionPane.showConfirmDialog(this, "Do you want to delete ID " + id + "?", "Accept", JOptionPane.YES_NO_OPTION) == 0) {
                    carDAO.deleteCar(id);
                    listele();
                    temizleGiris();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: The ID to be deleted must be a number.");
            }
        });

        // 4. ARAMA
        btnSearch.addActionListener(e -> {
            String aranan = txtSearch.getText();
            displayArea.setText("--- Search: " + aranan + " ---\n");
            List<String> sonuclar = carDAO.searchCars(aranan);
            
            if (sonuclar == null || sonuclar.isEmpty()) {
                displayArea.append("Cannot Found.");
            } else {
                for(String s : sonuclar) displayArea.append(s + "\n");
            }
        });

        // 5. LİSTELEME
        btnViewAll.addActionListener(e -> {
            txtSearch.setText("");
            listele();
        });

        // 6. TEMİZLEME
        btnClear.addActionListener(e -> temizleGiris());
    }

    private void listele() {
        displayArea.setText("");
        List<String> list = carDAO.getAllCars();
        if (list != null) {
            for(String s : list) displayArea.append(s + "\n");
        }
    }

    private void temizleGiris() {
        txtId.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        txtYear.setText("");
        txtPrice.setText("");
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // İŞTE ÇÖZÜM: Hem "Specific Exception" (Multi-catch) hem de "Logger" kullandık.
            // VS Code artık hiçbir şeye itiraz edemez.
            Logger.getLogger(SmartDriveApp.class.getName()).log(Level.SEVERE, null, e);
        }

        SwingUtilities.invokeLater(() -> new SmartDriveApp().setVisible(true));
    }
}