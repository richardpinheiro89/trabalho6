package mongoproject;

import java.sql.SQLException;

//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

public class principal {

	public static MongoDatabase db2;
	public static MongoClient client;
	//public static MongoCollection<org.bson.Document> colection;
	
	
	private static MongoDatabase conectar (String tipo, String serverdb ) {
		if (tipo == "open"){
			    client = MongoClients.create(serverdb);
		    	db2 = client.getDatabase("test");
		    	try {
		    		System.out.println("conexao OK");
		    	} catch (MongoException me) { 
		    		System.out.println("problema na conexao");
			    }
		}
		else if (tipo == "close") {
			client.close();
			System.out.println("Fechar banco de dados");
		}
	    return db2;
	}
	
	private void listaProdutos(MongoDatabase db) {
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
			
	}
	
	private void insereProduto(MongoDatabase db,String idProduto, String nome, String descricao, String valor, String estado) {
		Document doc = new Document();
		doc.append("_id", idProduto);
		doc.append("nome",nome);
		doc.append("descricao",descricao);
		doc.append("valor",valor);
		doc.append("estado",estado);
		try {
		db.getCollection("produtos").insertOne(doc);
		} catch (MongoException me) {
			System.err.println("Não é possivél fazer inclusão");
		}
		return;
	}
	
	private void alteraValorProduto(MongoDatabase db, String idProduto, String val) {	
		Document docquery = new Document().append("_id",idProduto);
		Bson updates = Updates.set("valor",val);
		UpdateOptions options = new UpdateOptions ().upsert(true);
		try {
			db.getCollection("produtos").updateOne(docquery,updates,options);
		} catch (MongoException me) {
			System.err.println("Não é possivél fazer alteração");
		}
		return;
	}
	
	private void apagaProduto(MongoDatabase db, String idProduto) {
		Document docquery = new Document().append("_id",idProduto);
		try {
			db.getCollection("produtos").deleteOne(docquery);
		} catch (MongoException me) {
			System.err.println("Não é possivél apagar");
		}
		return;
	}
	
	public static void main(String[] args) {
		principal loja = new principal();
		MongoDatabase sera2 =  conectar("open","mongodb://localhost");
		System.out.println("Lista Original de Produtos");
		// Lista os produtos da Loja
		if (!sera2.equals(null)) loja.listaProdutos(sera2);
		// Insere novo produto	
		if (!sera2.equals(null)) loja.insereProduto(sera2,"7", "Prod7", "Bla Bla", "500.0", "Bla Bla");
		System.out.println("Lista com Novo Produto");
		// Lista os produtos da Loja
		if (!sera2.equals(null)) loja.listaProdutos(sera2);
		// Altera valor do produto
		if (!sera2.equals(null)) loja.alteraValorProduto(sera2, "7","400.0");
		System.out.println("Lista com Valor do Produto Alterado");
		// Lista com produto alterado
		if (!sera2.equals(null)) loja.listaProdutos(sera2);
		System.out.println("Apaga Produto Número 7");
		// Apaga produto 
		if (!sera2.equals(null)) loja.apagaProduto(sera2,"7");
		System.out.println("Volta a Lista Original de Produtos");
		// Lista os produtos da Loja
		if (!sera2.equals(null)) loja.listaProdutos(sera2);
		try {
			sera2 =  conectar("close","mongodb://localhost");
		} catch (MongoException me) {
			System.out.println("Erro ao fechar conexão : " + me);
			me.printStackTrace();
		}	
	}
}
