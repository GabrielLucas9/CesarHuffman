package criptografia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

class HuffmanNode implements Comparable<HuffmanNode> {
    int frequency;
    char data;
    HuffmanNode left, right;
    @Override
    public int compareTo(HuffmanNode node) {
        return frequency - node.frequency;
    }
}

public class Criptografia {
    private static final Map<Character, String> charPrefixHashMap = new HashMap<>();
    static HuffmanNode root;
    private static HuffmanNode buildTree(Map<Character, Integer> freq) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        Set<Character> keySet = freq.keySet();
        for (Character c : keySet) {
            HuffmanNode huffmanNode = new HuffmanNode();
            huffmanNode.data = c;
            huffmanNode.frequency = freq.get(c);
            huffmanNode.left = null;
            huffmanNode.right = null;
            priorityQueue.offer(huffmanNode);
        }
        assert priorityQueue.size() > 0;
        while (priorityQueue.size() > 1) {
            HuffmanNode x = priorityQueue.peek();
            priorityQueue.poll();
            HuffmanNode y = priorityQueue.peek();
            priorityQueue.poll();
            HuffmanNode sum = new HuffmanNode();
            sum.frequency = x.frequency + y.frequency;
            sum.data = '-';
            sum.left = x;
            sum.right = y;
            root = sum;
            priorityQueue.offer(sum);
        }
        return priorityQueue.poll();
    }

    private static void setPrefixCodes(HuffmanNode node, StringBuilder prefix) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                charPrefixHashMap.put(node.data, prefix.toString());

            } else {
                prefix.append('0');
                setPrefixCodes(node.left, prefix);
                prefix.deleteCharAt(prefix.length() - 1);

                prefix.append('1');
                setPrefixCodes(node.right, prefix);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

    }

    private static String encode(String test, PrintWriter gravararvo) {
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < test.length(); i++) {
            if (!freq.containsKey(test.charAt(i))) {
                freq.put(test.charAt(i), 0);
            }
            freq.put(test.charAt(i), freq.get(test.charAt(i)) + 1);
        }
        
        gravararvo.println("Character Frequency Map = " + freq);
        System.out.println("Character Frequency Map = " + freq);
        
        root = buildTree(freq);
        setPrefixCodes(root, new StringBuilder());
        
        gravararvo.println("Character Prefix Map = " + charPrefixHashMap);
        System.out.println("Character Prefix Map = " + charPrefixHashMap);
      
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < test.length(); i++) {
            char c = test.charAt(i);
            s.append(charPrefixHashMap.get(c));
        }
        return s.toString();
    }

    private static void decode(String s, PrintWriter descriptografado, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        HuffmanNode temp = root;
        //System.out.println("Encoded: " + s);
        for (int i = 0; i < s.length(); i++) {
            int j = Integer.parseInt(String.valueOf(s.charAt(i)));
            if (j == 0) {
                temp = temp.left;
                if (temp.left == null && temp.right == null) {
                    stringBuilder.append(temp.data);
                    temp = root;
                }
            }
            if (j == 1) {
                temp = temp.right;
                if (temp.left == null && temp.right == null) {
                    stringBuilder.append(temp.data);
                    temp = root;
                }
            }
        }
        String mensagem = stringBuilder.toString();
        descriptografado.println("A mensagem descriptografada (Huffman) é " + mensagem);
        System.out.println("A mensagem descriptografada (Huffman) é " + mensagem);
        
        char [] men = mensagem.toCharArray();
        
        System.out.printf("A mensagem totalmente descriptografada (Huffman+Cesar) é: ");
        descriptografado.printf("A mensagem totalmente descriptografada (Huffman+Cesar) é: ");
        
        for(int i = 0; i < mensagem.length(); i++){
            men[i] = (char) (men[i] - n);
            descriptografado.printf("%c", men[i]);
            System.out.printf("%c", men[i]);
        }

    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        String nome = "Texto.txt";
        int n = 0;
        try {
            FileReader arq = new FileReader(nome);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();
            FileWriter arqcripto = new FileWriter("C:\\Users\\gabri\\OneDrive\\Documentos\\NetBeansProjects\\Criptografia\\CriptografadoCesar.txt");
            PrintWriter gravarArq = new PrintWriter(arqcripto);
            System.out.println("Indique um inteiro para a criptografia de César: ");
            n = ler.nextInt();
            System.out.println("Executando a cifra de César:");
            System.out.println("...");
            n = n % 26;
            while (linha != null) {
                char[] cod = linha.toCharArray();
                for (int i = 0; i < linha.length(); i++) {
                    cod[i] += n;
                }
                String novalinha = "";
                for (int i = 0; i < linha.length(); i++) {
                    novalinha = novalinha + cod[i];
                }
                gravarArq.println(novalinha);
                System.out.printf("%s\n", novalinha);
                linha = lerArq.readLine();
            }
            arq.close();
            arqcripto.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }
        nome = "CriptografadoCesar.txt";
        try {
            FileReader arq = new FileReader(nome);
            BufferedReader lerArq = new BufferedReader(arq);
            
            FileWriter arqcripto = new FileWriter("C:\\Users\\gabri\\OneDrive\\Documentos\\NetBeansProjects\\Criptografia\\CriptografadoHuffman.txt");
            PrintWriter gravarArq = new PrintWriter(arqcripto);
            
            String linha = lerArq.readLine();
            System.out.println("Executando a criptografia de Huffman:");
            System.out.println("...");
            
            String test = "";
            
            while (linha != null) {
                test += linha;
                linha = lerArq.readLine();
            }
            
            FileWriter arqarvo = new FileWriter("C:\\Users\\gabri\\OneDrive\\Documentos\\NetBeansProjects\\Criptografia\\ArvoreHuffman.txt");
            PrintWriter gravararvo = new PrintWriter(arqarvo);
            
            gravararvo.printf("Cesar: %d", n);
            gravararvo.println("");
            
            String s = encode(test, gravararvo);
            gravarArq.println(s);
            
            FileWriter arqdescripto = new FileWriter("C:\\Users\\gabri\\OneDrive\\Documentos\\NetBeansProjects\\Criptografia\\Descriptografado.txt");
            PrintWriter descriptografado = new PrintWriter(arqdescripto);
            
            //System.out.println(s);
            
            decode(s, descriptografado, n);
            
            arq.close();
            arqarvo.close();
            arqcripto.close();
            descriptografado.close();
            
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }
        System.out.println();
    }
}
