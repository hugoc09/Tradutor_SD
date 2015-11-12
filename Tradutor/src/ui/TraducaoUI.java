package ui;

import java.util.List;

import javax.swing.JOptionPane;

import network.TranslateListener;

public class TraducaoUI implements TranslateListener {

	
	@Override
	public void traducao(List<String> palavraTraduzida) {
		String palavra = palavraTraduzida.get(0);

		JOptionPane.showMessageDialog(null, palavra, "Tradução Português - Inglês",
				JOptionPane.INFORMATION_MESSAGE);
	}
	

}
