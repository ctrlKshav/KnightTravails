import  java.util.*;
//ganpatC

class Coordinates{
    int x,y;
    Coordinates parent;
    Coordinates(int x,int y,Coordinates parent){
        this.x=x;
        this.y=y;
        this.parent=parent;
    }
}

class ChessBoard{

    int boardSize;
    char[][] chessboard ;

    ChessBoard(){
        boardSize=8;
        chessboard= new char[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                chessboard[i][j] = '-';
            }
        }
    }


    void printChessBoard(Coordinates inital,Coordinates target){

        // Display the empty chessboard

        System.out.println("  ───────────────────");
        for (int i = 0; i < boardSize; i++) {
            System.out.print((boardSize - i) + " │ ");
            for (int j = 0; j < boardSize; j++) {
                //Displaying the Chessboard with Either 1 or 2 Knights
                if (inital != null && i==inital.x && j==inital.y) {
                        System.out.print("K ");
                        continue;
                }
                if (target != null && i==target.x && j==target.y) {
                    System.out.print("K ");
                    continue;
                }
                   
                System.out.print(chessboard[i][j] + " ");
            }
            System.out.println("│ ");
        }
        System.out.println("  ───────────────────");

        System.out.println("    A B C D E F G H");
    }

}

public class KnightTravails {

    //Coordinates of Knight's to and from

    static Coordinates inital,target;

    //Method to convert A8 to (0,0) and H1 to (7,7)
    static void convertChessNotationToCoords(){
    
        ChessBoard cb=new ChessBoard();
        
        Scanner sc=new Scanner(System.in);
        cb.printChessBoard(null,null);
        System.out.println("Knight's Starting Position");
        char rank,file;
        while (true) {
            String user = sc.nextLine().trim().toUpperCase();
            if(user.length()==2) {
                file = user.charAt(0);
                rank = user.charAt(1);
                if ((file >= 'A' && file <= 'H') && (rank >= '1' && rank <= '8'))
                    break;
            }
            System.out.println("Please Enter Valid Chess Notation");
        }
        //Chess coordinates go bottom to up whereas array go up to bottom
        int rowInitial = '8' - rank;
        int columnInitial = file - 'A';
        inital=new Coordinates(rowInitial,columnInitial,null);

        cb.printChessBoard(inital,null);

        System.out.println("Knight's Ending Position");
        while (true) {
            String user = sc.nextLine().trim().toUpperCase();
            if(user.length()==2) {
                file = user.charAt(0);
                rank = user.charAt(1);
                if ((file >= 'A' && file <= 'H') && (rank > '0' && rank < '9'))
                    break;
            }
            System.out.println("Please Enter Valid Chess Notation");
        }
        //Chess coordinates go bottom to up whereas array go up to botoom
        int rowTarget = '8' - rank;
        int columnTarget = file - 'A';
        target=new Coordinates(rowTarget,columnTarget,null);
        cb.printChessBoard(inital,target);
    }

    //Checks if the coordinate is outside the chess board
    public static boolean isValid(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public static List<Coordinates> findShortestPath(Coordinates initial,Coordinates target) {
        //A knight has max 8 possible moves 
        int[][] possibleMoves = {
                {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2},
                {1, -2}, {2, -1}, {2, 1}, {1, 2}
        };


        Queue<Coordinates> queue = new LinkedList<>();
        
        //A boolean 2D array to keep track of the visited squares
        boolean[][] visited = new boolean[8][8];

        Coordinates startNode = initial;
        queue.add(startNode);
        visited[initial.x][initial.y] = true;

        while (!queue.isEmpty()) {
            Coordinates current = queue.poll();

            if (current.x == target.x && current.y == target.y)
            //Found the target
                return reconstructPath(current);

            //Not the target check for 8 other possibilites
            for (int[] move : possibleMoves) {
                int newX = current.x + move[0];
                int newY = current.y + move[1];

                if (isValid(newX, newY) && !visited[newX][newY]) {
                    //If it's valid and not visisted we enqueue the coordinate
                    Coordinates newPoint = new Coordinates(newX, newY, current);
                    queue.add(newPoint);
                    visited[newX][newY] = true;
                }
            }
        }

        return null; // No path found
    }

    //Returns the move sequence which the Knight played to reach the target
    public static List<Coordinates> reconstructPath(Coordinates target) {
        List<Coordinates> path = new ArrayList<>();
        Coordinates current = target;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {

        ChessBoard cb=new ChessBoard();
        convertChessNotationToCoords();

        Scanner sc=new Scanner(System.in);
        //Move sequence returned
        List<Coordinates> path=findShortestPath(inital,target);
        if(path!=null) {
            System.out.println();
            System.out.println("The Minimum number of moves required by the Knight to reach the target square is : " + (path.size() - 1));
            System.out.println("Press Enter to see the Move Sequence");
            sc.nextLine();
            System.out.println("Starting Position");

            for (Coordinates c : path) {
                if (c.parent != null) {
                    //ReConverting the 2D array coordinate to Chess notation
                    char rank = (char) ('8' - (char) c.x);
                    char file = (char) ('A' + (char) c.y);
                    System.out.println("Press Enter to Play " + file + "" + rank);
                    sc.nextLine();
                }
                cb.printChessBoard(c, null);

            }
        }
        else{
            System.out.println("No Path Found");
        }


    }
}
