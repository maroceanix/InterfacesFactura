package factura_swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import java.awt.Color;

public class Factura extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField asuntoTf;
	private JTextField cantTf;
	private DefaultListModel<FacturaClase> modelo = new DefaultListModel<>();
	private JList<FacturaClase> listaFacturas = new JList<>(modelo);

	private JLabel asuntoLbl;
	private JLabel fechaLbl;
	private JLabel cantidadLbl;
	private JLabel tipoLbl;
	private JLabel erroresConfirmaciones;


	private JSpinner dia;
	private JSpinner mes;
	private JSpinner anio;

	private JButton aniadirBtn;
	private JButton editarBtn;
	private JButton eliminarBtn;
	private int indiceEditando = -1; // -1 = no estamos editando


	private JComboBox tipoCB;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Factura frame = new Factura();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Factura() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 719, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		asuntoLbl = new JLabel("Asunto:");
		asuntoLbl.setHorizontalAlignment(SwingConstants.LEFT);
		asuntoLbl.setBounds(10, 32, 69, 23);
		contentPane.add(asuntoLbl);

		fechaLbl = new JLabel("Fecha:");
		fechaLbl.setHorizontalAlignment(SwingConstants.LEFT);
		fechaLbl.setBounds(10, 87, 69, 23);
		contentPane.add(fechaLbl);

		cantidadLbl = new JLabel("Cantidad:");
		cantidadLbl.setHorizontalAlignment(SwingConstants.LEFT);
		cantidadLbl.setBounds(10, 138, 69, 23);
		contentPane.add(cantidadLbl);

		tipoLbl = new JLabel("Tipo:");
		tipoLbl.setHorizontalAlignment(SwingConstants.LEFT);
		tipoLbl.setBounds(10, 175, 69, 23);
		contentPane.add(tipoLbl);

		dia = new JSpinner();
		dia.setBounds(89, 89, 96, 20);
		contentPane.add(dia);

		mes = new JSpinner();
		mes.setBounds(195, 89, 96, 20);
		contentPane.add(mes);

		anio = new JSpinner();
		anio.setBounds(301, 89, 96, 20);
		contentPane.add(anio);

		listaFacturas.setBounds(25, 208, 379, 164);
		contentPane.add(listaFacturas);

		aniadirBtn = new JButton("Añadir/Actualizar");
		aniadirBtn.setBounds(493, 235, 123, 39);
		contentPane.add(aniadirBtn);
		aniadirBtn.addActionListener(this);
		aniadirBtn.setEnabled(false);

		editarBtn = new JButton("Editar");
		editarBtn.setBounds(493, 284, 123, 39);
		contentPane.add(editarBtn);
		editarBtn.addActionListener(this);
		// false para que estén deshabilitados
		editarBtn.setEnabled(false);

		eliminarBtn = new JButton("Eliminar");
		eliminarBtn.setBounds(493, 333, 123, 39);
		contentPane.add(eliminarBtn);
		eliminarBtn.addActionListener(this);
		eliminarBtn.setEnabled(false);

		asuntoTf = new JTextField();
		asuntoTf.setBounds(89, 34, 96, 18);
		contentPane.add(asuntoTf);
		asuntoTf.setColumns(10);
		
		//para que no deje escribir más de x caracteres
		asuntoTf.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (asuntoTf.getText().length() > 9) {
					e.consume();
				}
			}
		});
		
		asuntoTf.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) { comprobarCampos(); }
		    public void removeUpdate(DocumentEvent e) { comprobarCampos(); }
		    public void insertUpdate(DocumentEvent e) { comprobarCampos(); }
		});

		cantTf = new JTextField();
		cantTf.setBounds(89, 140, 96, 18);
		contentPane.add(cantTf);
		cantTf.setColumns(10);
		cantTf.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) { comprobarCampos(); }
		    public void removeUpdate(DocumentEvent e) { comprobarCampos(); }
		    public void insertUpdate(DocumentEvent e) { comprobarCampos(); }
		});

		tipoCB = new JComboBox();
		tipoCB.setModel(new DefaultComboBoxModel(new String[] { "Empresa", "Particulares" }));
		tipoCB.setBounds(89, 176, 96, 20);
		contentPane.add(tipoCB);
		
		erroresConfirmaciones = new JLabel(".");
		erroresConfirmaciones.setForeground(new Color(255, 0, 128));
		erroresConfirmaciones.setBounds(246, 371, 258, 23);
		contentPane.add(erroresConfirmaciones);
		erroresConfirmaciones.setVisible(false);
		//añado el método de comprobar si están todos los campos rellenos en el combobox
		tipoCB.addActionListener(e -> comprobarCampos());
		configurarListenersLista(); 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == aniadirBtn) {
			String asunto = asuntoTf.getText();
			int diaN = (int) dia.getValue();
			int mesN = (int) mes.getValue();
			int anioN = (int) anio.getValue();
			int cantidad = Integer.parseInt(cantTf.getText());
			String tipo = tipoCB.getSelectedItem().toString();
			FacturaClase f = new FacturaClase(tipo, asunto, cantidad, anioN, mesN, diaN);
			if (indiceEditando != -1) {
		        // Estamos editando: reemplazamos la factura existente
		        modelo.set(indiceEditando, f);
		        indiceEditando = -1; // reseteamos
		        erroresConfirmaciones.setVisible(true);
		        erroresConfirmaciones.setText("Factura actualizada con éxito!!");
		    } else {
		        // Añadir nueva factura
		        modelo.addElement(f);
		        erroresConfirmaciones.setVisible(true);
		        erroresConfirmaciones.setText("Factura añadida con éxito!!");
		    }

		    // Limpiar campos si quieres
		    asuntoTf.setText("");
		    cantTf.setText("");
		    tipoCB.setSelectedIndex(0);
		
			
			
		}
		if (e.getSource() == editarBtn) {
			int index= listaFacturas.getSelectedIndex();
			if(index!=-1) {
				FacturaClase f=listaFacturas.getSelectedValue();
				asuntoTf.setText(f.getAsunto_fac());
				cantTf.setText(String.valueOf(f.getCantidad_fac()));
				tipoCB.setSelectedItem(f.getTipo_fac());
				dia.setValue(f.getFecha_fac().getDayOfMonth());
				mes.setValue(f.getFecha_fac().getMonthValue());
				anio.setValue(f.getFecha_fac().getYear());
				
				indiceEditando = index;
			}
			
		}
		if (e.getSource() == eliminarBtn) {
			int index = listaFacturas.getSelectedIndex(); // índice seleccionado

		    if (index != -1) {  // si hay algo seleccionado
		        modelo.remove(index);  // lo borro del modelo
		    }
		    listaFacturas.clearSelection();
		    editarBtn.setEnabled(false);
		    eliminarBtn.setEnabled(false);
		    erroresConfirmaciones.setVisible(true);
			erroresConfirmaciones.setText("Factura eliminada con éxito!!");

		}

	}
	//compruebo si todos los campos están rellenos, y este método lo añado a cada vez que relleno un campo
	private void comprobarCampos() {
	    boolean completo = !asuntoTf.getText().isEmpty() && validarCantidadEntero(cantTf.getText()) && tipoCB.getSelectedItem() != null;
	    aniadirBtn.setEnabled(completo);
	}
	//no deja añadir una cantidad si no es un int
	private boolean validarCantidadEntero(String s) {
	    try {
	        Integer.parseInt(s);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}

	public void configurarListenersLista() {
		listaFacturas.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {

				if (!e.getValueIsAdjusting()) {

					boolean haySeleccion = listaFacturas.getSelectedIndex() != -1;

					editarBtn.setEnabled(haySeleccion);
					eliminarBtn.setEnabled(haySeleccion);
				}
			}
		});

	}
}
