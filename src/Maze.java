import java.io.*;
import java.util.Scanner;
import java.util.Stack;

public class Maze {
    static int pradziaX;
    static int pradziaY;
    static Stack<Rez> stack = new Stack<Rez>();
    static int m, n; // labirinto matmenys
    static int lab[][]; // labirintas (1 - siena, 0 - laisvas langelis)
    static int cx[] = {0,-1,0,1,0}; // 4 produkcijos - x ir y poslinkiai
    static int cy[] = {0,0,-1,0,1};
    static int l; // ejimo eiles nr. (zymi aplankyta langeli)
    static boolean yra = false;
    static BufferedWriter bw;
    static File fout;
    static FileOutputStream fos;
    static int labVar = 1; // 1 - is pav. , 2 - 20X15.
    static int labKob[][]; // globalus masyvas
    static int fx[], fy[]; // masyvai fronto virsunems
    static int uzd; // skaitliukas uzd
    static int k; // produkcijos nr
    static int x,y;// pradine keliautojo padietis
    static int u,v;
    static int nauja; // skaitliukas atidaromai virsunei
    static char quotes ='"';
    static int banga = 0;
    static String tab = "    ";
    static int tempBanga = 0;


    public static void inicializuoti() throws Exception{
        // nuskaitau labirinta is failo, suzinau m ir n
        String fileName = "";
        if(labVar == 2){
            fileName = "lab2.txt";
        }else{
            fileName = "lab1.txt";
        }
        FileReader fr = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fr);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line =  bufferedReader.readLine()) != null){
            stringBuffer.append(line);
            stringBuffer.append("\n");
            n++;
        }

        int index = 0;
        while (stringBuffer.charAt(index) != '\n'){
            if(stringBuffer.charAt(index) != ' '){
                m++;
            }
            index++;
        }

        // labirinto uzpildymas
        lab = new int [m + 1] [n + 1]; // m + 1 ir n + 1, nes indeksai nuo 1
        labKob = new int [m + 1] [n + 1];
        index = 0;
        for(int i = n; i > 0; i--){  // per y asi
            for (int j = 1; j <= m; j++){ // per x asi
                if(stringBuffer.charAt(index) == '\n'){
                    index++;
                }
                lab[j][i] = Character.getNumericValue(stringBuffer.charAt(index));
                labKob[j][i] = Character.getNumericValue(stringBuffer.charAt(index));
                index = index + 2;
            }
        }
        fr.close();
    }


    public /*static*/ void atgal(int u, int v) throws Exception{ // Surenkamas kelias (u,v - isejimo is labirinto koordinates)
        System.out.println();
        //Idetu pabaiga
        Rez temp;
        temp = new Rez(0, u, v);
        stack.push(temp);
        bw.write('\n');
        //System.out.println("ISEJIMAS" + u + v);
        int k;
        lab[u][v] = labKob[u][v]; // pazymimas isejimo langelis
        k = 5;

        do{ // perrenkamos 4 produkcijo. Ieskomas toks langelis. LABKOB[uu,vv], kurio reiksme vienetu mazesne uz LABKOB[u,v]
            k = k - 1;
          // System.out.println("K" + k);
            int uu = u + cx[k];
            int vv = v + cy[k];

            if((1 <= uu) && (uu <= m) && (1 <= vv) && (vv <= n)){ // neiseiname uz labirinto
                if(labKob[uu][vv] == labKob[u][v] - 1){

                    switch(k) {
                        case 1:
                            k = 3;
                            break;
                        case 2:
                            k = 4;
                            break;
                        case 3:
                            k = 1;
                            break;
                        case 4:
                            k = 2;
                            break;
                    }

                    //System.out.println("K" + k);
                    temp = new Rez(k,uu,vv);
                    stack.push(temp);
                    lab[uu][vv] = labKob[u][v]; // pazymime langeli masyve LAB
                    u = uu;
                    v = vv;
                    k = 5;
                }
            }
        }while(!(labKob[u][v] == 2));

    }

    public static void bangosAlgoritmas() throws Exception{
        if((x == 1)||(x == m)||(y == 1)||(y == n)){ // jeigu ant krasto terminalineje busenoje, tai baigti darba
            yra = true;
            u = x;
            v = y;
        }
        int prev = 0;
        banga++;

        if((x > 1) && (x < m) && (y > 1) && (y < n)){
            do{ // ciklas per virsunes

                x = fx[uzd];
                y = fy[uzd];
                k = 0;

                if(labKob[x][y] - 1 != prev){
                    l++;
                    System.out.println("BANGA " + (labKob[x][y] - 1) + ", žymė L="+ quotes + l + quotes);
                    bw.write("BANGA " + (labKob[x][y] - 1) + ", žymė L="+ quotes + l + quotes + "\n");
                    prev = labKob[x][y] - 1;
                }

               // System.out.println("BANGA temp" + tempBanga);
                System.out.println(tab + "Uždaroma UZD=" + uzd + ", " + "X=" + x + ", " + "Y=" + y + ".");
                bw.write(tab + "Uždaroma UZD=" + uzd + ", " + "X=" + x + ", " + "Y=" + y + "." + "\n");

                do{ // ciklas per 4 produkcijas
                    k++;
                    u = x + cx[k];
                    v = y + cy[k];

                    System.out.print(tab + tab + "R" + k + ". X=" + u +", " +  "Y=" + v + ". ");
                    bw.write(tab + tab + "R" + k + ". X=" + u +", " +  "Y=" + v + ". ");

                    if(labKob[u][v] == 0) {   // jeigu kelias laisvas
                        labKob[u][v] = labKob[x][y] + 1; // naujos bangos nr.

                        if ((u == 1) || (u == m) || (v == 1) || (v == n)) { // krastas
                            yra = true; // sekme, galimas kviesti atgal
                            nauja++;

                            System.out.print("Laisva. Nauja=" + nauja + ". " + "Terminalinė." + "\n");
                            bw.write("Laisva. Nauja=" + nauja + ". " + "Terminalinė." + "\n");

                        } else { // nauja virsune talpinama i fronto pabaiga
                            nauja = nauja + 1;

                            System.out.print("Laisva. Nauja=" + nauja + "." + "\n");
                            bw.write("Laisva. Nauja=" + nauja + "." + "\n");

                            fx[nauja] = u;
                            fy[nauja] = v;
                        }
                    }else{
                        if(lab[u][v] == 1){
                            System.out.println("Siena.");
                            bw.write("Siena." + "\n");
                        }else{
                            System.out.println("UŽDARYTA arba ATIDARYTA.");
                            bw.write("UŽDARYTA arba ATIDARYTA." + "\n");
                        }
                    }

                }while (!((yra) || (k == 4))); // perinktos 4 produkcijos isejimas rastas

                System.out.println();
                bw.write("\n");
                uzd = uzd + 1; // bus uzdaroma (skleidziama) kita virsune

            }while (!((uzd > nauja) || (yra)));
        }
    }

    public static void atspausdintiLabirinta() throws Exception{
        System.out.println(tab + "Y, V ^");
        bw.write(tab + "Y, V ^" + '\n');
        for(int i = n; i >= 1; i--) {
            System.out.print(String.format(tab + "%4s",i) + " | ");
            bw.write(String.format(tab + "%4s",i) + " | ");
            for(int j = 1; j <= m; j++) {
                System.out.print(String.format("%3s",labKob[j][i]) + " ");
                bw.write(String.format("%3s",labKob[j][i]) + " ");
            }
            System.out.println();
            bw.write('\n');
        }
        System.out.println(tab +"     -----------------------------------------------------------------------------------------> X, U");
        System.out.println();

        bw.write((tab +"     --------------------------------------------------------------------------------------------------> X, U"));
        bw.write('\n');


        for(int i = 1; i <= m; i++) {
            if(i ==  1) {
                System.out.print(tab + String.format("%10s", i));
                bw.write(tab + String.format("%10s", i));
            }else {
                System.out.print(String.format("%4s", i));
                bw.write(String.format("%4s", i));
            }
        }

    }

    public static void main(String args []) throws Exception{
        Maze mm = new Maze();
        System.out.println("Iveskite labirinto varianta: \n" + tab + "1 - labirintas 7.3 pav \n" + tab +
                "2 - 20 X 15 labirintas, kuriame kelias viršyja 100 višūnių.");
        Scanner sc = new Scanner(System.in);
        labVar = sc.nextInt();

        long startTime = System.currentTimeMillis();
        fout = new File("out.txt");
        fos = new FileOutputStream(fout);
        bw = new BufferedWriter(new OutputStreamWriter(fos));

        inicializuoti();

        // Ivedama pradine keliautojo padietis !!!!!

        x = 5;
        y = 4;
        l = 2;
        pradziaX = x;
        pradziaY = y;
        labKob[x][y] = l;
        lab [x][y] = l;

        System.out.println("1 DALIS. Duomenys");
        System.out.println(tab + "1.1. Labirintas" + '\n');

        bw.write("1 DALIS. Duomenys");
        bw.write("\n" + tab + "1.1. Labirintas" + "\n \n");

        atspausdintiLabirinta();

        System.out.println("\n\n" + tab + "1.2. Pradinė padėtis X=" + x + ", Y=" + y  + ", L=" + l + "."+ '\n');
        bw.write("\n\n" + tab + "1.2. Pradinė padėtis X=" + x + ", Y=" + y  + ", L=" + l + "."+ "\n\n");

        System.out.println("2 DALIS. Vykdymas \n");
        bw.write("2 DALIS. Vykdymas" + "\n\n");

        fx = new int [m * n + 1];
        fy = new int [m * n + 1];
        fx[1] = x;
        fy[1] = y;
        uzd = 1;
        nauja = 1;
        yra = false;

        System.out.println("BANGA " + banga +", žymė L="+ quotes + l + quotes + ". Pradinė padėtis X=" + x + ", " + "Y=" + y + ", " + "NAUJA=" + nauja + ".\n");
        bw.write("BANGA " + banga +", žymė L="+ quotes + l + quotes + ". Pradinė padėtis X=" + x + ", " + "Y=" + y + ", " + "NAUJA=" + nauja + ".\n\n");
        bangosAlgoritmas();

        if(yra){
            System.out.println("3 Dalis Rezultatai");
            System.out.println(tab + "3.1 Kelias rastas." + "\n");
            System.out.println(tab + "3.2 Kelias grafiškai." + '\n');


            bw.write(" \n 3 Dalis Rezultatai" + '\n');
            bw.write(tab + "3.1 Kelias rastas." + '\n');
            bw.write(tab + "3.2 Kelias grafiškai." + "\n \n");

            bw.write("LABCOPY" +'\n');
            System.out.println("LABCOPY");

            atspausdintiLabirinta();
            mm.atgal(u,v);

            System.out.print("\n \n" + tab + "3.3 Kelias taisyklėmis: ");
            bw.write("\n \n" + tab + "3.3 Kelias taisyklėmis: ");

            int temp = stack.size();
            String kelias =" ";
            for(int i = 0; i < temp; i++){
                Rez r = stack.pop();

                if(r.r != 0) {
                    if(i != temp - 2){
                        System.out.print("R" + r.r + ", ");
                        bw.write("R" + r.r + ", ");
                        kelias = kelias +"[X="+ r.u +",Y=" + r.v + "], ";
                    }else{
                        System.out.print("R" + r.r + ".");
                        bw.write("R" + r.r + ", ");
                        kelias = kelias +"[X="+ r.u +",Y=" + r.v + "], ";
                    }
                }else {
                    kelias = kelias +"[X="+ r.u +",Y=" + r.v + "]. ";
                }
            }

            System.out.println("\n\n" + tab + "3.4 Kelias viršūnėmis:" + kelias);
            bw.write("\n\n"  + tab + "3.4 Kelias viršūnėmis:" + kelias + '\n');

        }else{
            System.out.println("\n \n 3 Dalis Rezultatai");
            System.out.println(tab  + "Kelias neegzistuoja.");

            bw.write("\n \n 3 Dalis Rezultatai" + '\n');
            bw.write(tab  + "Kelias neegzistuoja.");

        }

        /*
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("Vykdymo laikas " + (totalTime /1000)+ "s.");
        bw.write("Vykdymo laikas " + (totalTime /1000) + "s.");
        */

        sc.close();
        bw.close();
    }
    class Rez {
        private int r,u,v;
        Rez(int r, int u, int v){
            this.r = r;
            this.u = u;
            this.v = v;
        }
    }
}
