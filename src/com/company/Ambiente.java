package com.company;

import java.util.Random;

class Ambiente {
    private int tamanho;
    private int limiteDeTempo;
    private int numPortas;
    private Pessoa[] pessoas;
    private int[] portasX;
    private int[] portasY;
    private int pessoasRestantes;

    public Ambiente(int tamanho, int limiteDeTempo, int numPessoas, int numPortas) {
        this.tamanho = tamanho;
        this.limiteDeTempo = limiteDeTempo;
        this.numPortas = numPortas;
        pessoas = new Pessoa[numPessoas];
        portasX = new int[numPortas];
        portasY = new int[numPortas];
        pessoasRestantes = numPessoas;

        for (int i = 0; i < numPortas; i++) {
            portasX[i] = tamanho - 1;
            portasY[i] = (tamanho / (numPortas + 1)) * (i + 1);
        }

        for (int i = 0; i < numPessoas; i++) {
            pessoas[i] = new Pessoa("Pessoa " + i, this);
            pessoas[i].start();
        }
    }

    public synchronized void moverPessoa(Pessoa pessoa, int x, int y) {
        // validar se a posição desejada é valida
        if (x >= 0 && x < tamanho && y >= 0 && y < tamanho) {
            for (Pessoa outraPessoa : pessoas) {
                // validar se a posição desejada já está ocupada
                if (outraPessoa != null && outraPessoa != pessoa && outraPessoa.getX() == x && outraPessoa.getY() == y) {
                    return;
                }
            }
            pessoa.setPosition(x, y);
        }
    }

    public synchronized void saidaBemSucedida(Pessoa pessoa) {
        pessoa.setPosition(-1, -1);
        System.out.println("Uma pessoa saiu com sucesso do ambiente.");
        pessoasRestantes--;

        if (todasSairam()) {
            System.out.println("Parabéns, o ambiente foi esvaziado com sucesso!");
            System.exit(0);
        }
    }

    public boolean todasSairam() {
        for (Pessoa pessoa : pessoas) {
            if (pessoa != null) {
                return false;
            }
        }
        return true;
    }

    public void mostrarAmbiente() {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                boolean pessoaNoLocal = false;
                boolean portaNoLocal = false;
                for (Pessoa pessoa : pessoas) {
                    if (pessoa != null && pessoa.getX() == i && pessoa.getY() == j) {
                        System.out.print("X ");
                        pessoaNoLocal = true;
                        break;
                    }
                }
                for (int k = 0; k < numPortas; k++) {
                    if (i == portasX[k] && j == portasY[k]) {
                        System.out.print("P ");
                        portaNoLocal = true;
                        break;
                    }
                }
                if (!pessoaNoLocal && !portaNoLocal) {
                    System.out.print(". ");
                }
            }
            System.out.println("");
        }
    }

    public int getTamanho() {
        return tamanho;
    }

    public int getNumPortas() {
        return numPortas;
    }

    public int getPortaX(int index) {
        return portasX[index];
    }

    public int getPortaY(int index) {
        return portasY[index];
    }

    public int getPessoasRestantes() {
        return pessoasRestantes;
    }
}