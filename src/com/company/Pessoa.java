package com.company;

import java.util.Random;

class Pessoa extends Thread {
    private String nome;
    private Ambiente ambiente;
    private int x;
    private int y;

    public Pessoa(String nome, Ambiente ambiente) {
        this.nome = nome;
        this.ambiente = ambiente;
        this.x = new Random().nextInt(ambiente.getTamanho());
        this.y = new Random().nextInt(ambiente.getTamanho());
    }

    @Override
    public void run() {
        while (true) {
            if (x == -1 && y == -1) {
                break; // Pessoa já saiu do ambiente
            }

            // Movimento aleatório: uma casa por vez
            int dx = new Random().nextInt(3) - 1; // -1, 0 ou 1
            int dy = new Random().nextInt(3) - 1; // -1, 0 ou 1

            int newX = x + dx;
            int newY = y + dy;

            ambiente.moverPessoa(this, newX, newY);

            try {
                Thread.sleep(1000); // Delay para simular a movimentação
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Verificar se a pessoa está em qualquer uma das portas e sair
            for (int k = 0; k < ambiente.getNumPortas(); k++) {
                if (x == ambiente.getPortaX(k) && y == ambiente.getPortaY(k)) {
                    ambiente.saidaBemSucedida(this);
                    return;
                }
            }
        }
    }

    public String getNome() {
        return nome;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}