import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

class LeitorCsv {
  public static Veiculo[] ler(String caminhoArquivo) {
    Veiculo[] veiculos = new Veiculo[10000];
    int n = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
      String linha = br.readLine();
      while ((linha = br.readLine()) != null) {
        veiculos[n++] = Veiculo.parseVeiculo(linha);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    Veiculo[] resultado = new Veiculo[n];
    System.arraycopy(veiculos, 0, resultado, 0, n);
    return resultado;
  }
}

class Data {
  private int ano;
  private int mes;
  private int dia;

  Data(int ano, int mes, int dia) {
    this.ano = ano;
    this.mes = mes;
    this.dia = dia;
  }

  String format() {
    return String.format("%02d/%02d/%04d", dia, mes, ano);
  }

  static Data parseData(String s) {
    String buffData[] = s.split("-");
    int ano = Integer.parseInt(buffData[0]);
    int mes = Integer.parseInt(buffData[1]);
    int dia = Integer.parseInt(buffData[2]);
    return new Data(ano, mes, dia);
  }
}

class Veiculo {
  private int id;
  private String marca;
  private String modelo;
  private int ano;
  private String categoria;
  private String combustivel[];
  private int cilindros;
  private double cilindrada;
  private String transmissao;
  private String tracao;
  private double consumoCidade;
  private double consumoEstrada;
  private double co2;
  private boolean turbo;
  private Data dataRegistro;

  public String getMarca() {
    return this.marca;
  }

  public double getConsumoCidade() {
    return this.consumoCidade;
  }

  public String getCategoria() {
    return this.categoria;
  }

  public int getId() {
    return this.id;
  }

  Veiculo(int id, String marca, String modelo, int ano, String categoria, String combustivel[], int cilindros,
      double cilindrada, String transmissao, String tracao, double consumoCidade, double consumoEstrada, double co2,
      boolean turbo, Data dataRegistro) {
    this.id = id;
    this.marca = marca;
    this.modelo = modelo;
    this.ano = ano;
    this.categoria = categoria;
    this.combustivel = combustivel;
    this.cilindros = cilindros;
    this.cilindrada = cilindrada;
    this.transmissao = transmissao;
    this.tracao = tracao;
    this.consumoCidade = consumoCidade;
    this.consumoEstrada = consumoEstrada;
    this.co2 = co2;
    this.turbo = turbo;
    this.dataRegistro = dataRegistro;
  }

  String format() {
    String combustivelStr = "[";
    for (int i = 0; i < combustivel.length; i++) {
      combustivelStr += combustivel[i];
      if (i + 1 != combustivel.length)
        combustivelStr += ",";
    }
    combustivelStr += "]";

    String cCidade = String.format(Locale.US, "%.2f", consumoCidade);
    String cEstrada = String.format(Locale.US, "%.2f", consumoEstrada);

    return "[" + id + " ## " + marca + " ## " + modelo + " ## " + ano + " ## " + categoria + " ## " + combustivelStr
        + " ## " + cilindros + " ## " + cilindrada + " ## " + transmissao + " ## " + tracao + " ## " + cCidade
        + " ## " + cEstrada + " ## " + co2 + " ## " + turbo + " ## " + dataRegistro.format() + "]";
  }
  // id,marca,modelo,ano,categoria,combustivel,cilindros,cilindrada,transmissao,tracao,consumo_cidade,consumo_estrada,co2,turbo,data_registro

  static Veiculo parseVeiculo(String s) {
    String buff[] = s.split(",");
    int id = Integer.parseInt(buff[0]);
    String marca = buff[1];
    String modelo = buff[2];
    int ano = Integer.parseInt(buff[3]);
    String categoria = buff[4];
    String combustivel[] = buff[5].split(";");
    int cilindros = Integer.parseInt(buff[6]);
    double cilindrada = Double.parseDouble(buff[7]);
    String transmissao = buff[8];
    String tracao = buff[9];
    double consumoCidade = Double.parseDouble(buff[10]);
    double consumoEstrada = Double.parseDouble(buff[11]);
    double co2 = Double.parseDouble(buff[12]);
    boolean turbo = Boolean.parseBoolean(buff[13]);
    Data dataRegistro = Data.parseData(buff[14]);
    return new Veiculo(id, marca, modelo, ano, categoria, combustivel, cilindros, cilindrada, transmissao, tracao,
        consumoCidade, consumoEstrada, co2, turbo, dataRegistro);
  }
}

public class ordenacaoMerge {
  public static void mergeSort(Veiculo[] v) {
    if (v == null || v.length <= 1)
      return;
    Veiculo[] auxiliar = new Veiculo[v.length];
    mergeSort(v, auxiliar, 0, v.length - 1);
  }

  public static void mergeSort(Veiculo[] v, Veiculo[] aux, int inicio, int fim) {
    if (inicio >= fim)
      return;
    int meio = inicio + (fim - inicio) / 2;
    mergeSort(v, aux, inicio, meio);
    mergeSort(v, aux, meio + 1, fim);

    merge(v, aux, inicio, meio, fim);
  }

  public static void merge(Veiculo[] v, Veiculo[] aux, int inicio, int meio, int fim) {
    for (int i = inicio; i <= fim; i++) {
      aux[i] = v[i];
    }
    int e = inicio;
    int d = meio + 1;

    for (int i = inicio; i <= fim; i++) {
      if (e > meio) {
        v[i] = aux[d++];
      } else if (d > fim) {
        v[i] = aux[e++];
      } else if (aux[e].getConsumoCidade() < aux[d].getConsumoCidade()) {
        v[i] = aux[e++];
      } else if (aux[e].getConsumoCidade() > aux[d].getConsumoCidade()) {
        v[i] = aux[d++];
      } else {
        int cmp = aux[e].getCategoria().compareTo(aux[d].getCategoria());
        if (cmp < 0) {
          v[i] = aux[e++];
        } else {
          v[i] = aux[d++];
        }
      }
    }
  }

  public static void main(String[] args) {
    Veiculo[] veiculos = LeitorCsv.ler("/tmp/veiculos.csv");
    Veiculo[] selecionados = new Veiculo[1000];
    int id, totalSelecionados = 0;

    Scanner sc = new Scanner(System.in);
    String input;
    while (sc.hasNext()) {
      input = sc.next();
      if (input.equals("-1"))
        break;

      id = Integer.parseInt(input);
      for (Veiculo v : veiculos) {
        if (v.getId() == id) {
          selecionados[totalSelecionados++] = v;
        }
      }
    }
    Veiculo[] selFinal = new Veiculo[totalSelecionados];
    System.arraycopy(selecionados, 0, selFinal, 0, totalSelecionados);
    mergeSort(selFinal);

    for (Veiculo v : selFinal) {
      System.out.println(v.format());
    }

  }

}
