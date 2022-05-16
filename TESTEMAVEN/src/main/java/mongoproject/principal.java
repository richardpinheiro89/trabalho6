package mongoproject;

//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;

import org.bson.Document;
import org.bson.conversions.Bson;
//import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

public class principal {

	//public static MongoCollection<org.bson.Document> colecao;
	public static MongoDatabase db2;
	
	public static MongoDatabase conectar (String tipo, String serverdb ) {
		if (tipo == "open"){
			try (MongoClient client = MongoClients.create(serverdb)){
				db2 = client.getDatabase("test");
				try {
					System.out.println("conexao OK");
				} catch (MongoException me) 
				{
					System.out.println("problema na conexao");
				}
			}				
		}
		else if (tipo == "close") {
			System.out.println("Fechar banco de dados");
		}
		return db2;
	}
	
	public static void listaProdutos(MongoDatabase db2) {
  	    MongoClient client = MongoClients.create("mongodb://localhost");
		MongoDatabase db = client.getDatabase("test");
		MongoCollection<org.bson.Document> colection = db.getCollection("produtos");
		Iterable<org.bson.Document> produtos = colection.find();
		for (org.bson.Document produto: produtos) {
			String nome = produto.getString("nome");
			String descricao = produto.getString("descricao");
			String valor = produto.getString("valor");
			String estado =  produto.getString("estado");
			System.out.println(nome + " -- " + descricao + " -- " + valor + " -- " + estado);
		}
		if (produtos == null) {
			System.out.println("Lista vazia");
		} else {
			System.out.println("--------");
		}
		
		client.close();
	}
	
	public static void insereProduto(MongoDatabase db2,String idProduto, String nome, String descricao, String valor, String estado) {
	    MongoClient client = MongoClients.create("mongodb://localhost");
		MongoDatabase db = client.getDatabase("test");
		Document doc = new Document();
		doc.append("_id", idProduto);
		doc.append("nome",nome);
		doc.append("descricao",descricao);
		doc.append("valor",valor);
		doc.append("estado",estado);
		db.getCollection("produtos").insertOne(doc);
		client.close();
		return;
	}
	
	public static void alteraValorProduto(MongoDatabase db2, String idProduto, String val) {	
	    MongoClient client = MongoClients.create("mongodb://localhost");
		MongoDatabase db = client.getDatabase("test");
		Document docquery = new Document().append("_id",idProduto);
		Bson updates = Updates.set("valor",val);
		UpdateOptions options = new UpdateOptions().upsert(true);
		try {
			db.getCollection("produtos").updateOne(docquery,updates,options);
		} catch (MongoException me) {
			System.err.println("Não é possivél fazer alteração");
		}
		client.close();
		return;
	}
	
	public static void apagaProduto(MongoDatabase db2, String idProduto) {
	    MongoClient client = MongoClients.create("mongodb://localhost");
		MongoDatabase db = client.getDatabase("test");
		Document docquery = new Document().append("_id",idProduto);
		try {
			db.getCollection("produtos").deleteOne(docquery);
		} catch (MongoException me) {
			System.err.println("Não é possivél apagar");
		}
		client.close();
		return;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MongoDatabase sera2 = conectar("open","mongodb://localhost");
		System.out.println("Lista Original de Produtos");
		// Lista os produtos da Loja
		if (!sera2.equals(null)) listaProdutos(sera2);
		// Insere novo produto	
		if (!sera2.equals(null)) insereProduto(sera2,"7", "Prod7", "Bla Bla", "500.0", "Bla Bla");
		System.out.println("Lista com Novo Produto");
		// Lista os produtos da Loja
		if (!sera2.equals(null)) listaProdutos(sera2);
		// Altera valor do produto
		if (!sera2.equals(null)) alteraValorProduto(sera2, "7","400.0");
		System.out.println("Lista com Valor do Produto Alterado");
		// Lista com produto alterado
		if (!sera2.equals(null)) listaProdutos(sera2);
		System.out.println("Apaga Produto Número 7");
		// Apaga produto 
		if (!sera2.equals(null)) apagaProduto(sera2,"7");
		System.out.println("Volta a Lista Original de Produtos");
		// Lista os produtos da Loja
		if (!sera2.equals(null)) listaProdutos(sera2);
	}
}
