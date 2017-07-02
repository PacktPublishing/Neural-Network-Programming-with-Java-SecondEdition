package edu.packt.neuralnet.examples.weather;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.packt.neuralnet.math.RandomNumberGenerator;

public class WeatherGUI extends JPanel {

	private JFrame frameGUI;
	private JLabel lbParameters;
	private JLabel lbDataset;
	private JComboBox cbDataset;
	private JLabel lbNeuronsHdnLayer;
	private JLabel lbLearningRate;
	private JLabel lbDataNorm;
	private JTextField txtNeuronsHdnLayer;
	private JSlider sldLearningRate;
	private JLabel lbMomentumRate;
	private JSlider sldMomentumRate;
	private JComboBox cbNormType;
	private JLabel lbMaxEpochs;
	private JTextField txtMaxEpochs;
	private JLabel lbMinOverallError;
	private JTextField txtMinOverallError;
	private JCheckBox chkPlaySound;
	private JButton btTrain;
	
	public void setupAndShowGUI() {
		final JFrame frameGUI = new JFrame("Weather GUI - Backpropagation");
		
		lbParameters = new JLabel("Set backpropagation training parameters:");

		lbDataset = new JLabel("Dataset (city):");
		String[] arrayDataset = { "Cruzeiro do Sul", "Picos", "Campos do Jordao", "Porto Alegre" };
		cbDataset = new JComboBox(arrayDataset);

		lbNeuronsHdnLayer = new JLabel("Neurons in hidden layer");
		txtNeuronsHdnLayer = new JTextField();

		lbLearningRate = new JLabel("Learning rate (0.1)");
		sldLearningRate = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
		sldLearningRate.setMajorTickSpacing(10);
		sldLearningRate.setMinorTickSpacing(1);
		sldLearningRate.setPaintTicks(true);
		sldLearningRate.setPaintLabels(true);
		sldLearningRate.setFont(new Font(frameGUI.getName(), Font.PLAIN, 8));
		sldLearningRate.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slide = (JSlider) e.getSource();
				double value = slide.getValue() / 100.0;
				lbLearningRate.setText("Learning rate (" + value + ")");
			}
		});
		
		lbMomentumRate = new JLabel("Momentum rate (0.7)");
		sldMomentumRate = new JSlider(JSlider.HORIZONTAL, 0, 100, 70);
		sldMomentumRate.setMajorTickSpacing(10);
		sldMomentumRate.setMinorTickSpacing(1);
		sldMomentumRate.setPaintTicks(true);
		sldMomentumRate.setPaintLabels(true);
		sldMomentumRate.setFont(new Font(frameGUI.getName(), Font.PLAIN, 8));
		sldMomentumRate.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slide = (JSlider) e.getSource();
				double value = slide.getValue() / 100.0;
				lbMomentumRate.setText("Momentum rate (" + value + ")");
			}
		});
		

		lbDataNorm = new JLabel("Data normalization type");
		String[] arrayDataNorm = { "1) MIN_MAX [-1.0, 1.0]", "2) MIN_MAX [0.0, 1.0]", "3) Z-SCORE [1.0]", "4) Z-SCORE [0.5]" };
		cbNormType = new JComboBox(arrayDataNorm);
		
		lbMaxEpochs = new JLabel("Maximum of Epochs");
		txtMaxEpochs = new JTextField();
		
		lbMinOverallError = new JLabel("Minimum Overall Error");
		txtMinOverallError = new JTextField();
		
		chkPlaySound = new JCheckBox("Beep when training finish");

		btTrain = new JButton("Start Training");

		lbParameters.setFont(new Font(frameGUI.getName(), Font.BOLD, 16));
		lbParameters.setBounds(40, 20, 400, 20);

		lbDataset.setBounds(40, 50, 190, 20);
		cbDataset.setBounds(40, 70, 190, 20);

		lbNeuronsHdnLayer.setBounds(40, 100, 190, 20);
		txtNeuronsHdnLayer.setBounds(40, 120, 190, 20);
		txtNeuronsHdnLayer.setText("5");

		lbLearningRate.setBounds(40, 150, 190, 20);
		sldLearningRate.setBounds(40, 170, 190, 50);
		
		lbMomentumRate.setBounds(40, 220, 190, 20);
		sldMomentumRate.setBounds(40, 240, 190, 50);

		lbDataNorm.setBounds(40, 290, 190, 20);
		cbNormType.setBounds(40, 310, 190, 20);
		
		lbMaxEpochs.setBounds(40, 340, 190, 20);
		txtMaxEpochs.setBounds(40, 360, 190, 20);
		txtMaxEpochs.setText("300");
		
		lbMinOverallError.setBounds(40, 390, 190, 20);
		txtMinOverallError.setBounds(40, 410, 190, 20);
		txtMinOverallError.setText("0.01");
		
		chkPlaySound.setBounds(40, 440, 300, 20);
		chkPlaySound.setSelected(true);
		
		btTrain.setBounds(40, 490, 140, 20);

		btTrain.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					new Thread( new WeatherChartsWindow(
							cbDataset.getSelectedItem().toString(), 
							Integer.parseInt(txtNeuronsHdnLayer.getText().toString()), 
							sldLearningRate.getValue() / 100.0, 
							cbNormType.getSelectedItem().toString(),
							Integer.parseInt(txtMaxEpochs.getText().toString()),
							sldMomentumRate.getValue() / 100.0,
							Double.parseDouble(txtMinOverallError.getText().toString()),
							chkPlaySound.isSelected() )
						).start();
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, 
							"Error! Details: " + e2.getMessage(),
							"Parameter Error",
							JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		frameGUI.add(lbParameters);
		frameGUI.add(lbDataset);
		frameGUI.add(lbNeuronsHdnLayer);
		frameGUI.add(lbLearningRate);
		frameGUI.add(lbDataNorm);
		frameGUI.add(lbMomentumRate);
		frameGUI.add(txtNeuronsHdnLayer);
		frameGUI.add(sldLearningRate);
		frameGUI.add(sldMomentumRate);
		frameGUI.add(cbNormType);
		frameGUI.add(cbDataset);
		frameGUI.add(lbMaxEpochs);
		frameGUI.add(txtMaxEpochs);
		frameGUI.add(lbMinOverallError);
		frameGUI.add(txtMinOverallError);
		frameGUI.add(chkPlaySound);
		frameGUI.add(btTrain);
		
		frameGUI.setLayout(null);
		frameGUI.pack();
		frameGUI.setLocationByPlatform(true);
		frameGUI.setSize(500, 600);
		frameGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frameGUI.setVisible(true);

	}


}
