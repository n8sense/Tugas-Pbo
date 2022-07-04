package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class HeroViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public HeroViewFrame(){
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        hapusButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int pilihan = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin?",
                    "Konfirmasi hapus data",
                    JOptionPane.YES_NO_OPTION
            );

            if (pilihan == 0 ){
                TableModel tm = viewTable.getModel();
                String idString = tm.getValueAt(barisTerpilih,0).toString();
                int id = Integer.parseInt(idString);

                String deleteSQL = "DELETE FROM hero WHERE id = ?";
                Connection c = Koneksi.getConnection();
                PreparedStatement ps;
                try {
                    ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        cariButton.addActionListener(e -> {
            String keyword = "%" + cariTextField.getText() + "%";
            String keyword1 = "%" + cariTextField.getText() + "%";
            String keyword2 = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT hero.id, kec_serang.kecepatan, hero.nama, tipe.tipe_hero " +
                    "FROM ((hero INNER JOIN kec_serang ON hero.id_kec = kec_serang.id) " +
                    "INNER JOIN tipe ON hero.id_tipe = tipe.id) " +
                    "WHERE tipe_hero like ? OR nama like ? OR kecepatan like ? ";

            Connection c = Koneksi.getConnection();
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ps.setString(2, keyword1);
                ps.setString(3, keyword2);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[4];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    row[2] = rs.getString("kec_serang");
                    row[3] = rs.getString("tipe_hero");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        tambahButton.addActionListener(e->{
            HeroInputFrame InputFrame= new HeroInputFrame();
            InputFrame.setVisible(true);
        });
        ubahButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            TableModel tm = viewTable.getModel();
            String idString = tm.getValueAt(barisTerpilih,0).toString();
            int id = Integer.parseInt(idString);

            HeroInputFrame inputFrame = new HeroInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        isiTable();
        init();
    }

    public void init(){
        setTitle("Data Hero");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void isiTable(){
        String selectSQL = "SELECT hero.id, kec_serang.kecepatan, hero.nama, tipe.tipe_hero " +
                "FROM ((hero INNER JOIN kec_serang ON hero.id_kec = kec_serang.id) " +
                "INNER JOIN tipe ON hero.id_tipe = tipe.id)";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String header[] = {"Id","Nama Hero","Kecepatan Serangan","Tipe Hero"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
            viewTable.getColumnModel().getColumn(0).setMinWidth(32);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);

            Object[] row = new Object[4];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                row[2] = rs.getString("kecepatan");
                row[3] = rs.getString("tipe_hero");
                dtm.addRow(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
