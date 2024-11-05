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
            pessoas[i] = new Pessoa(String.valueOf(i), this);
            pessoas[i].start();
        }
    }

    public synchronized void moverPessoa(Pessoa pessoa, int x, int y) {
        if (x >= 0 && x < tamanho && y >= 0 && y < tamanho) {
            for (Pessoa outraPessoa : pessoas) {
                if (outraPessoa != null && outraPessoa != pessoa && outraPessoa.getX() == x && outraPessoa.getY() == y) {
                    return;
                }
            }
            pessoa.setPosition(x, y);
        }
    }

    public synchronized void saidaBemSucedida(Pessoa pessoa) {
        pessoa.setPosition(-1, -1);
        System.out.println("\nA pessoa " + pessoa.getNome() + " saiu com sucesso do ambiente.\n");
        pessoasRestantes--;

        if (todasSairam()) {
            System.out.println("Parabéns, o ambiente foi esvaziado com sucesso!");
            System.exit(0);
        }
    }

    public boolean todasSairam() {
        for (Pessoa pessoa : pessoas) {
            if (pessoa != null && pessoa.getX() != -1 && pessoa.getY() != -1) {
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
                        System.out.print(pessoa.getNome() + " ");
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
        System.out.println("");
    }

    public void moverParaPortaMaisProxima() {
        for (Pessoa pessoa : pessoas) {

            if (pessoa != null && pessoa.getX() != -1 && pessoa.getY() != -1) {
                int[] portaMaisProxima = encontrarPortaMaisProxima(pessoa);
                moverPessoaParaPorta(pessoa, portaMaisProxima[0], portaMaisProxima[1]);
            }
        }
    }

    private int[] encontrarPortaMaisProxima(Pessoa pessoa) {
        int menorDistancia = Integer.MAX_VALUE;
        int portaX = -1, portaY = -1;

        for (int k = 0; k < numPortas; k++) {
            int dx = Math.abs(pessoa.getX() - portasX[k]);
            int dy = Math.abs(pessoa.getY() - portasY[k]);
            int distancia = dx + dy;

            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                portaX = portasX[k];
                portaY = portasY[k];
            }
        }
        return new int[]{portaX, portaY};
    }

    private synchronized void moverPessoaParaPorta(Pessoa pessoa, int portaX, int portaY) {
        while (pessoa.getX() != portaX || pessoa.getY() != portaY) {
            if (pessoa.getX() == -1 && pessoa.getY() == -1) {
                return; // Pessoa já saiu do ambiente
            }
            int newX = pessoa.getX();
            int newY = pessoa.getY();

            if (newX < portaX) newX++;
            else if (newX > portaX) newX--;

            if (newY < portaY) newY++;
            else if (newY > portaY) newY--;

            moverPessoa(pessoa, newX, newY);
            mostrarAmbiente(); // Mostrar o ambiente após cada movimento
            try {
                Thread.sleep(1000); // Pausa para cada passo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        saidaBemSucedida(pessoa);
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