import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

class LeitorCsv{
	public static Veiculo[] ler(String caminhoArquivo){
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
class Data{
	private int ano;
	private int mes;
	private int dia;

	Data(int ano, int mes, int dia){
		this.ano = ano;
		this.mes = mes;
		this.dia = dia;
	}
	String format(){
		return dia+"/"+mes+"/"+ano;
	}

	static Data parseData(String s){
		String buffData[] = s.split("-");
		int ano = Integer.parseInt(buffData[0]);
		int mes = Integer.parseInt(buffData[1]);
		int dia = Integer.parseInt(buffData[2]);
		return new Data(ano,mes,dia);
	}
}

class Veiculo{
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

	public String getMarca(){
		return this.marca;
	}
	
	Veiculo(int id, String marca, String modelo, int ano, String categoria, String combustivel[], int cilindros, double cilindrada, String transmissao, String tracao, double consumoCidade, double consumoEstrada, double co2,boolean turbo, Data dataRegistro){
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


	String format(){
		String combustivelStr = "[";
		for (int i = 0; i < combustivel.length; i++) {
			combustivelStr += combustivel[i];
			if(i+1 != combustivel.length) combustivelStr += ",";
		}
		combustivelStr+= "]";


		return "[" + id + " ## " + marca + " ## " + modelo + " ## " + ano + " ## " + categoria + " ## " + combustivelStr + " ## " + cilindros + " ## " + cilindrada + " ## " + transmissao + " ## " + tracao + " ## " + consumoCidade + " ## " + consumoEstrada + " ## " + co2 + " ## " + turbo + " ## " + dataRegistro + "]";
	}

	// id,marca,modelo,ano,categoria,combustivel,cilindros,cilindrada,transmissao,tracao,consumo_cidade,consumo_estrada,co2,turbo,data_registro
	
	static Veiculo parseVeiculo(String s){
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
		boolean turbo = buff[13] == "true"? true : false;
	       	Data dataRegistro = Data.parseData(buff[14]);
		return new Veiculo(id,marca,modelo,ano,categoria,combustivel,cilindros,cilindrada,transmissao,tracao,consumoCidade,consumoEstrada,co2,turbo,dataRegistro);
	}
}



public class ordenacaoMerge{
	public static void main(String[] args){
		Veiculo[] veiculos = LeitorCsv.ler("/tmp/veiculos.csv");
		System.out.println(veiculos[0].getMarca());
	}

}
