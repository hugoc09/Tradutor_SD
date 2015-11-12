package ui;

import javax.swing.JOptionPane;

import network.NetworkManagement;

public class Principal {
	
	public static void main(String[] args) {

		String palavra = JOptionPane.showInputDialog(null, "Digite a palavra a ser traduzida");

		System.out.println(palavra);
		
		NetworkManagement.traduzir(palavra);
	}

}
