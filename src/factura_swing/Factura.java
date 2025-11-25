package factura_swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.DateTimeException;
import java.time.LocalDate;

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
import javax.swing.SpinnerNumberModel;
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
	private int indiceEditando = -1;

	private JComboBox tipoCB;

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

		dia = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
		dia.setBounds(89, 89, 96, 20);
		contentPane.add(dia);

		mes = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
		mes.setBounds(195, 89, 96, 20);
		contentPane.add(mes);

		anio = new JSpinner((new SpinnerNumberModel(2025, 1900, 2100, 1)));
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
			cantTf.getDocument().addDocumentListener(new DocumentListener() {//obtiene el objeto que gestiona el texto escrito en el campo. document listener se activa cuando el contenido cambia
				public void changedUpdate(DocumentEvent e) { comprobarCampos(); }
				public void removeUpdate(DocumentEvent e) { comprobarCampos(); }
				public void insertUpdate(DocumentEvent e) { comprobarCampos(); }
			});

		tipoCB = new JComboBox();
		tipoCB.setModel(new DefaultComboBoxModel(new String[] { "Empresa", "Particulares" }));
		tipoCB.setBounds(89, 176, 96, 20);
		contentPane.add(tipoCB);
		tipoCB.addActionListener(e -> comprobarCampos());

		erroresConfirmaciones = new JLabel(".");
		erroresConfirmaciones.setForeground(new Color(255, 0, 128));
		erroresConfirmaciones.setBounds(246, 371, 258, 23);
		contentPane.add(erroresConfirmaciones);
		erroresConfirmaciones.setVisible(false);
		//cada vez que hago un cambio el listener salta
		dia.addChangeListener(e -> comprobarCampos());
		mes.addChangeListener(e -> comprobarCampos());
		anio.addChangeListener(e -> comprobarCampos());

		configurarListenersLista();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == aniadirBtn) {

			String asunto = asuntoTf.getText().trim();

			if (asunto.isEmpty()) {
				erroresConfirmaciones.setText("El asunto no puede estar vacío");
				erroresConfirmaciones.setVisible(true);
				return;
			}

			for (int i = 0; i < modelo.size(); i++) {
				FacturaClase fExist = modelo.get(i);
				if (fExist.getAsunto_fac().equals(asunto) && i != indiceEditando) {
					erroresConfirmaciones.setText("Ya existe una factura con ese asunto");
					erroresConfirmaciones.setVisible(true);
					return;
				}
			}

			int d = (int) dia.getValue();
			int m = (int) mes.getValue();
			int a = (int) anio.getValue();

			LocalDate fecha;
			try {
				fecha = LocalDate.of(a, m, d);
			} catch (DateTimeException ex) {
				erroresConfirmaciones.setText("La fecha introducida no es válida");
				erroresConfirmaciones.setVisible(true);
				return;
			}

			int cantidad = Integer.parseInt(cantTf.getText());
			String tipo = tipoCB.getSelectedItem().toString();

			FacturaClase f = new FacturaClase(tipo, asunto, cantidad, a, m, d);

			if (indiceEditando != -1) {
				modelo.set(indiceEditando, f);
				erroresConfirmaciones.setText("Factura actualizada con éxito!!");
				indiceEditando = -1;
			} else {
				modelo.addElement(f);
				erroresConfirmaciones.setText("Factura añadida con éxito!!");
			}

			erroresConfirmaciones.setVisible(true);

			asuntoTf.setText("");
			cantTf.setText("");
			tipoCB.setSelectedIndex(0);
			dia.setValue(1);
			mes.setValue(1);
			anio.setValue(LocalDate.now().getYear());

			editarBtn.setEnabled(false);
			eliminarBtn.setEnabled(false);

			listaFacturas.clearSelection();
		}

		if (e.getSource() == editarBtn) {

			int index = listaFacturas.getSelectedIndex();
			if (index != -1) {

				FacturaClase f = listaFacturas.getSelectedValue();

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

			int index = listaFacturas.getSelectedIndex();

			if (index != -1) {
				modelo.remove(index);
			}

			listaFacturas.clearSelection();
			editarBtn.setEnabled(false);
			eliminarBtn.setEnabled(false);

			erroresConfirmaciones.setVisible(true);
			erroresConfirmaciones.setText("Factura eliminada con éxito!!");
		}
	}

	//activO/desactivO el botón 
	private void comprobarCampos() {

		String asunto = asuntoTf.getText().trim();
		boolean asuntoValido = asunto.length() >= 1 && asunto.length() <= 10; 

		boolean cantidadValida = validarCantidadEntero(cantTf.getText());
		if (cantidadValida) {
			cantidadValida = Integer.parseInt(cantTf.getText()) > 0;
		}

		boolean tipoValido = tipoCB.getSelectedItem() != null;

		int d = (int) dia.getValue();
		int m = (int) mes.getValue();
		int a = (int) anio.getValue();

		boolean fechaValida;
		try {
			LocalDate.of(a, m, d);
			fechaValida = true;
		} catch (DateTimeException ex) {
			erroresConfirmaciones.setText("La fecha debe tener un formato válido");
			erroresConfirmaciones.setVisible(true);
			fechaValida = false;
		}

		boolean todoOK = asuntoValido && cantidadValida && tipoValido && fechaValida; //si todos estos oolean son true, todoOK será true también

		aniadirBtn.setEnabled(todoOK);
	}

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

