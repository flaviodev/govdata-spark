import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class DadosPanoramaIBGE {

	public static void main(String[] args) throws Exception {

		new DadosPanoramaIBGE().extraiDados();
	}

	private List<UF> criaUFs() {
		List<UF> ufs = new ArrayList<>();
		ufs.add(new UF("AC", "Acre"));
		ufs.add(new UF("AL", "Alagoas"));
		ufs.add(new UF("AP", "Amapá"));
		ufs.add(new UF("AM", "Amazonas"));
		ufs.add(new UF("BA", "Bahia"));
		ufs.add(new UF("CE", "Ceará"));
		ufs.add(new UF("DF", "Distrito Federal"));
		ufs.add(new UF("ES", "Espírito Santo"));
		ufs.add(new UF("GO", "Goiás"));
		ufs.add(new UF("MA", "Maranhão"));
		ufs.add(new UF("MT", "Mato Grosso"));
		ufs.add(new UF("MS", "Mato Grosso do Sul"));
		ufs.add(new UF("MG", "Minas Gerais"));
		ufs.add(new UF("PR", "Paraná"));
		ufs.add(new UF("PB", "Paraíba"));
		ufs.add(new UF("PA", "Pará"));
		ufs.add(new UF("PE", "Pernambuco"));
		ufs.add(new UF("PI", "Piauí"));
		ufs.add(new UF("RN", "Rio Grande do Norte"));
		ufs.add(new UF("RS", "Rio Grande do Sul"));
		ufs.add(new UF("RJ", "Rio de Janeiro"));
		ufs.add(new UF("RO", "Rondônia"));
		ufs.add(new UF("RR", "Roraima"));
		ufs.add(new UF("SC", "Santa Catarina"));
		ufs.add(new UF("SE", "Sergipe"));
		ufs.add(new UF("SP", "São Paulo"));
		ufs.add(new UF("TO", "Tocantins"));

		return ufs;
	}

	public void extraiDados() throws Exception {
                System.out.println("https://cidades.ibge.gov.br/brasil/<UF>/panorama");
		System.out.println("UF,Estado,População censo 2010,População estimada 2017,Matrículas no ensino fundamental 2015,Rendimento mensal domiciliar per capita 2016,IDH 2010");
		for (UF uf : criaUFs()) {
			try {
				extraiDadosUF(uf);
			} catch (Exception e) {
				System.out.println(uf.getSigla() + "," + uf.getNome() + ",<falha na obtenção dos dados>");
			}
		}
	}

	private Document getDocumentDaUF(String uf) throws IOException {
		return Jsoup.connect("https://cidades.ibge.gov.br/brasil/" + uf.toLowerCase() + "/panorama").get();
	}

	private void extraiDadosUF(UF uf) throws Exception {
		long populacaoUltimoCenso = getPopulacaoUltimoCenso(getDocumentDaUF(uf.getSigla()));
		long populacaoExtimada2017 = getPopulacaoExtimada2017(getDocumentDaUF(uf.getSigla()));
		long matriculasEnsinoFundamental2015 = getMatriculasEnsinoFundamental2015(getDocumentDaUF(uf.getSigla()));
		BigDecimal rendimentoNominalMensal2016 = getRendimentoNominalMensalDomiciliarPerCapita2016(
				getDocumentDaUF(uf.getSigla()));
		BigDecimal idh2010 = getIndiceDesenvolvimentoHumano2010(getDocumentDaUF(uf.getSigla()));

		System.out.println(uf.getSigla() + "," + uf.getNome() + "," + populacaoUltimoCenso + "," + populacaoExtimada2017
				+ "," + matriculasEnsinoFundamental2015 + "," + rendimentoNominalMensal2016 + "," + idh2010);
	}

	private Long getPopulacaoUltimoCenso(Document doc) {
		Elements elements = doc.body().getElementsContainingOwnText("População no último censo");
		String populacao = elements.get(0).parent().child(2).text();

		return new Long(populacao.replace(" pessoas", "").replace(".", ""));
	}

	private Long getPopulacaoExtimada2017(Document doc) {
		Elements elements = doc.body().getElementsContainingOwnText("População estimada");
		String populacao = elements.get(0).parent().child(2).text();

		return new Long(populacao.replace(" pessoas", "").replace(".", ""));
	}

	private Long getMatriculasEnsinoFundamental2015(Document doc) {
		Elements elements = doc.body().getElementsContainingOwnText("Matrículas no ensino fundamental");
		String matriculas = elements.get(0).parent().child(2).text();

		return new Long(matriculas.replace(" matrículas", "").replace(".", ""));
	}

	private BigDecimal getRendimentoNominalMensalDomiciliarPerCapita2016(Document doc) {
		Elements elements = doc.body().getElementsContainingOwnText("Rendimento nominal mensal domiciliar per capita");
		String rendimento = elements.get(0).parent().child(2).text();

		return new BigDecimal(rendimento.replace(" R$", "").replace(".", "").replace(",", "."));
	}

	private BigDecimal getIndiceDesenvolvimentoHumano2010(Document doc) {
		Elements elements = doc.body().getElementsContainingOwnText("Índice de Desenvolvimento Humano (IDH)");
		String idh = elements.get(0).parent().child(2).text();

		return new BigDecimal(idh.replace(",", "."));
	}
}

class UF {
	private String sigla;
	private String nome;

	public UF(String sigla, String nome) {
		this.sigla = sigla;
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
