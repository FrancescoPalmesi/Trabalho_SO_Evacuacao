package com.company;

public class SimulacaoVazao {
    public static void main(String[] args) {
        int tamanhoAmbiente = 10;
        int limiteDeTempo = 10; // 60 segundos
        int numPessoas = 10;
        int numPortas = 4;

        Ambiente ambiente = new Ambiente(tamanhoAmbiente, limiteDeTempo, numPessoas, numPortas);

        for (int segundo = 0; segundo < limiteDeTempo; segundo++) {
            ambiente.mostrarAmbiente();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int pessoasRestantes = ambiente.getPessoasRestantes();
        if (pessoasRestantes == 0) {
            System.out.println("Parabéns, o ambiente foi esvaziado com sucesso!");
            System.exit(0);
        } else {
            System.out.println(pessoasRestantes + " pessoa(s) não conseguiram achar a saída.");
            System.exit(1);
        }
    }
}