package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class HeroInputFrame extends JFrame {
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JComboBox kecCB;
    private JComboBox classCB;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public HeroInputFrame() {
        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            ComboBoxItem kec = (ComboBoxItem) kecCB.getSelectedItem();
            ComboBoxItem tipe = (ComboBoxItem) classCB.getSelectedItem();
            int id_kec = kec.getValue();
            int id_tipe = tipe.getValue();

            if(nama.equals("")){
                JOptionPane.showMessageDialog(
                        null,
                        "Lengkapi Nama Hero",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (this.id == 0) {
                    String cekSQL = "SELECT * FROM hero WHERE nama = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "Hero Sudah Ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String insertSQL = "INSERT INTO hero SET nama = ?, id_kec = ?, id_tipe = ?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id_kec);
                    ps.setInt(3, id_tipe);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String cekSQL = "SELECT * FROM hero WHERE nama=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "Hero Sudah Ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    String updateSQL = "UPDATE hero SET nama=?, id_kec=?, id_tipe=? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id_kec);
                    ps.setInt(3, id_tipe);
                    ps.setInt(4, id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        keccb();
        classcb();
        init();
    }

    public void init() {
        setTitle("Input Data");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void isiKomponen() {
        idTextField.setText(String.valueOf(this.id));

        String findSQL = "SELECT * FROM hero WHERE id = ?";

        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                namaTextField.setText(rs.getString("nama"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void keccb() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM kec_serang ORDER by kecepatan";

        Statement s = null;
        try {
            s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            kecCB.addItem(new ComboBoxItem(0, "Pilih Kecepatan"));
            while (rs.next()){
                kecCB.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("kecepatan")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public void classcb() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM tipe ORDER by tipe_hero";

        Statement s = null;
        try {
            s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            classCB.addItem(new ComboBoxItem(0, "Pilih Tipe"));
            while (rs.next()){
                classCB.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("tipe_hero")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
