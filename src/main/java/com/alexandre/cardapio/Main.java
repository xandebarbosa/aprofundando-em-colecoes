package com.alexandre.cardapio;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.alexandre.cardapio.ItemCardapio.CategoriaCardapio.ENTRADAS;
import static com.alexandre.cardapio.ItemCardapio.CategoriaCardapio.SOBREMESA;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Database database = new Database();
//        List<ItemCardapio> itens = database.listaItensCardapio();
//        itens.forEach(System.out::println);

        System.out.println("------------------------");

        //Usando a busca por id
        Optional<ItemCardapio> optionalItemCardapio = database.itemCardapioPorId(1L);
        if (optionalItemCardapio.isPresent()) {
            ItemCardapio itemCardapio = optionalItemCardapio.get();
            System.out.println(itemCardapio);
        } else {
            System.out.printf("Item não encontrado%n");
        }

        System.out.println("---------Estilo mais funcional, mai enxuto---------------");
        //Outro jeito de fazer com Estilo mais funcional, mai enxuto
        String mensagem = optionalItemCardapio.map(ItemCardapio::toString).orElse("Nâo Encontrado");
        System.out.println(mensagem);

        System.out.println("----------MANTENDO AS CATEGORIAS QUE ESTÃO EM PROMOÇÃO--------------");

        // MANTENDO AS CATEGORIAS QUE ESTÃO EM PROMOÇÃO
        Set<ItemCardapio.CategoriaCardapio> categoriasEmPromocao = new TreeSet<>();
        categoriasEmPromocao.add(SOBREMESA);
        categoriasEmPromocao.add(ENTRADAS);
        categoriasEmPromocao.forEach(System.out::println);

        //OUTRA MANIRA D FAZER, MAIS ENXUTO
        Set<ItemCardapio.CategoriaCardapio> categoriaCardapio2 = Set.of(SOBREMESA, ENTRADAS);  //Set.of - serve para colocar coisas que não mudam
        categoriaCardapio2.forEach(System.out::println);

        System.out.println("----------Categorias em Promoção com EnumSet--------------");
        System.out.println("----------Usar esse, Extremamente eﬁciente--------------");

        //Categorias em Promoção com EnumSet
        //Ordem do enum, semelhante ao TreeSet
        //Extremamente eﬁciente
        //Representado internamente com bits
        Set<ItemCardapio.CategoriaCardapio> categoriaEmPromocao = EnumSet.of(SOBREMESA, ENTRADAS);
        categoriaEmPromocao.forEach(System.out::println);

        // PRECISO DAS CATEGORIAS ASSOCIADA AS CATEGORIAS EM PROMOÇÃO
        //Categorias em Promoção com EnumMap
        //Extremamente eﬁciente
        //Representado internamente com bits
        System.out.println("----------Categorias em Promoção com EnumMap--------------");
        EnumMap<ItemCardapio.CategoriaCardapio, String> promocoes = new EnumMap<>(ItemCardapio.CategoriaCardapio.class);

        promocoes.put(ItemCardapio.CategoriaCardapio.SOBREMESA, "O doce perfeito para você!");
        promocoes.put(ItemCardapio.CategoriaCardapio.ENTRADAS, "Comece sua refeição com um toque de sabor!");
        String descricao = promocoes.get(ItemCardapio.CategoriaCardapio.ENTRADAS);
        System.out.printf("Entradas: %s\n", descricao);

        //PRECISO DE UM HISTÓRICO DE VISUALIZAÇÃO DO CARDÁPIO
        // Histórico de visualizações com HashMap
        System.out.println("----------HISTÓRICO DE VISUALIZAÇÃO DO CARDÁPIO--------------");
        //Usando o Histórico de visualizações
        HistoricoVisualizacao historico = new HistoricoVisualizacao(database);
        historico.registrarVisualizacao(1L); // refresco
        historico.registrarVisualizacao(2L); // sanduiche
        historico.registrarVisualizacao(6L); // pipoca
        historico.registrarVisualizacao(1L); // refresco (de novo)

        historico.listaVisualizacoes();
        historico.totalItensVisualizados();

        //----------------------------------------------------------------
        // PRECISO REMOVER UM ITEM DO CARDÁPIO
        System.out.println("----------REMOVENDO ITEM DO CARDÁPIO--------------");
        long idParaRemover = 1L;
        boolean removido = database.removeItemCardapio(idParaRemover); // refresco
        System.out.printf("Item %d %s\n", idParaRemover,
                (removido ? "removido" : "não encontrado"));
        System.out.println();
        System.out.println("-IMPRIMINDO A LISTAGEM EM TELA PARA CONFIRMAR SE O ITEM FOI REMOVIDO--");
        database.listaItensCardapio()
                .forEach(System.out::println); //IMPRIMINDO A LISTAGEM EM TELA PARA CONFIRMAR SE O ITEM FOI REMOVIDO

        System.out.println("--Item removido ainda no histórico--");

        System.out.println("Solicitando GC...");
        System.gc();

        Thread.sleep(500); // tempo para o GC agir
        historico.listaVisualizacoes();
        historico.totalItensVisualizados();

        System.out.println();
        //PRECISO ALTERAR O PREÇO DE UM ITEM DE CARDÁPIO
//        System.out.println("--ALTERANDO O PREÇO DE UM ITEM DE CARDÁPIO--");
//        System.out.println();
//        ItemCardapio item = database.itemCardapioPorId(9L).orElseThrow();
//        System.out.printf("'%s': R$ %.2f\n", item.nome(), item.preco());
//        boolean alterado = database.alteraPrecoItemCardapio(9L, new BigDecimal("2.99"));
//        System.out.printf("%s\n", alterado ? "Preço alterado." : "Não encontrado.");
//        ItemCardapio itemAlterado = database.itemCardapioPorId(9L).orElseThrow();
//        System.out.printf("'%s': R$ %.2f\n", itemAlterado.nome(), itemAlterado.preco());

        System.out.println();
        //PRECISO AUDITAR TODA MUDANÇA DE PREÇO DOS ITENS
        //database.rastroAuditoriaPrecos();

        //Mudando preços na Main
        boolean alterado1 = database.alteraPrecoItemCardapio(2L, new BigDecimal("3.99"));
        System.out.printf("%s\n", alterado1 ? "Preço alterado 1" : "Não encontrado.");
        boolean alterado2 = database.alteraPrecoItemCardapio(2L, new BigDecimal("2.99"));
        System.out.printf("%s\n", alterado2 ? "Preço alterado 2" : "Não encontrado.");
        boolean alterado3 = database.alteraPrecoItemCardapio(2L, new BigDecimal("4.99"));
        System.out.printf("%s\n", alterado3 ? "Preço alterado 3" : "Não encontrado.");
        database.rastroAuditoriaPrecos();

        System.out.println("------------------------");
        System.out.println();
        ItemCardapio item= database.itemCardapioPorId(7L).orElseThrow(); // 2.99
        database.alteraPrecoItemCardapio(7L, new BigDecimal("3.99")); // 2.99 => 3.99
        ItemCardapio item1 = database.itemCardapioPorId(7L).orElseThrow(); // 3.99
        database.alteraPrecoItemCardapio(7L, new BigDecimal("2.99")); // 3.99 => 2.99
        ItemCardapio item2 = database.itemCardapioPorId(7L).orElseThrow(); // 2.99
        database.alteraPrecoItemCardapio(7L, new BigDecimal("4.99")); // 2.99 => 4.99
        ItemCardapio item3 = database.itemCardapioPorId(7L).orElseThrow(); // 4.99
        //IdentityHashMap
        System.out.println("== " + (item == item2));
        System.out.println("equals() " + (item.equals(item2)));
        System.out.println("hashCode() " + (item.hashCode() == item2.hashCode()));

        //-------------------------------------------------------------
        // LinkedHashSet - Mantém a ordem de inserção - sem repetir elementos
        // saber quais a categorias realmente tenho no cardapio
//        Set<ItemCardapio.CategoriaCardapio> categorias = new LinkedHashSet<>();
//        for (ItemCardapio item : items) {
//            ItemCardapio.CategoriaCardapio categoria = item.categoria();
//            categorias.add(categoria);
//        }
//        for (ItemCardapio.CategoriaCardapio categoria : categorias) {
//            System.out.println(categoria);
//        }
//        System.out.println("-------------");
//        items.stream()
//                .map(ItemCardapio::categoria)
//                .collect(Collectors.toCollection(LinkedHashSet::new))
//                .forEach(System.out::println);
        //------------------------------------------------------LinkedHashSet---------------------

        // TreeSet - Mantém ordenado na ordem do enun(ItemCardapio) - não repete elementos
//        Comparator<ItemCardapio.CategoriaCardapio> categoriaCardapioComparator = Comparator.comparing(ItemCardapio.CategoriaCardapio::name);
//
//        Set<ItemCardapio.CategoriaCardapio> categoriasUnicas = new TreeSet<>(categoriaCardapioComparator);
//        for (ItemCardapio item : items) {
//            categoriasUnicas.add(item.categoria());
//        }
//        for (ItemCardapio.CategoriaCardapio categoria : categoriasUnicas) {
//            System.out.println(categoria);
//        }
//        System.out.println("-------------");
//        items.stream()
//                .map(ItemCardapio::categoria)
//                .collect(Collectors.toCollection(
//                        () -> new TreeSet<>(categoriaCardapioComparator)
//                ))
//                .forEach(System.out::println);
        //----------------------------------------------------------------------TreSet--

        //PRECISO SABER QUANTOS ITENS DE CADA CATEGORIA EXISTEM NO CARDÁPIO
        //Usando HashMap  não mantém a ordem de inserção
//        Map<ItemCardapio.CategoriaCardapio, Integer> itensPorCategoria = new HashMap<>();
//        for (ItemCardapio item : items) {
//            int quantidade;
//            if (itensPorCategoria.containsKey(item.categoria())) {
//                quantidade = itensPorCategoria.get(item.categoria()) + 1;
//            } else {
//                quantidade = 1;
//            }
//            itensPorCategoria.put(item.categoria(), quantidade);
//        }
//
//        for (ItemCardapio.CategoriaCardapio categoria : itensPorCategoria.keySet()) {
//            Integer quantidade = itensPorCategoria.get(categoria);
//            System.out.printf("%s: %d\n", categoria, quantidade);
//        }
//
//        System.out.println("------------------------------");
//
//        //HashMap com Stream API
//        items.stream()
//                .collect(Collectors.groupingBy(
//                        ItemCardapio::categoria,
//                        Collectors.counting()
//                ))
//                .forEach((categoria, quantidade) ->
//                        System.out.printf("%s: %d\n", categoria, quantidade));
// --------------------------------------------------------------------

        //LinkedHashMap para ordem das chaves
        //PRECISO SABER QUANTOS ITENS DE CADA CATEGORIA EXISTEM NO CARDÁPIO
//        Map<ItemCardapio.CategoriaCardapio, Integer> itensPorCategoria = new LinkedHashMap<>();
//        for (ItemCardapio item : items) {
//            int quantidade;
//            if (itensPorCategoria.containsKey(item.categoria())) {
//                quantidade = itensPorCategoria.get(item.categoria()) + 1;
//            } else {
//                quantidade = 1;
//            }
//            itensPorCategoria.put(item.categoria(), quantidade);
//        }
//
//        for (ItemCardapio.CategoriaCardapio categoria : itensPorCategoria.keySet()) {
//            Integer quantidade = itensPorCategoria.get(categoria);
//            System.out.printf("%s: %d\n", categoria, quantidade);
//        }
//
//        System.out.println("------------------------------");
//
//        //HashMap com Stream API
//        items.stream()
//                .collect(Collectors.groupingBy(
//                        ItemCardapio::categoria,
//                        LinkedHashMap::new,
//                        Collectors.counting()
//                ))
//                .forEach((categoria, quantidade) ->
//                        System.out.printf("%s: %d\n", categoria, quantidade));
        //--------------------------------------------------------------------

//        ItemCardapio item = database.itemCardapioPorId(1L).orElseThrow(); // 2.99
//
//        database.alteraPrecoItemCardapio(1L, new BigDecimal("3.99")); // 2.99 => 3.99
//        ItemCardapio item1 = database.itemCardapioPorId(1L).orElseThrow(); // 3.99
//
//        database.alteraPrecoItemCardapio(1L, new BigDecimal("2.99")); // 3.99 => 2.99
//        ItemCardapio item2 = database.itemCardapioPorId(1L).orElseThrow(); // 2.99
//
//        database.alteraPrecoItemCardapio(1L, new BigDecimal("4.99")); // 2.99 => 4.99
//        ItemCardapio item3 = database.itemCardapioPorId(1L).orElseThrow(); // 4.99
//
//        System.out.println("== " + (item == item2));
//        System.out.println("equals() " + (item.equals(item2)));
//        System.out.println("hashCode() " + (item.hashCode() == item2.hashCode()));
//
//        database.rastroAuditoriaPrecos();
    }
}