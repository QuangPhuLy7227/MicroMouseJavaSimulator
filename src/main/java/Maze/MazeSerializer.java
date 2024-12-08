package Maze;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.List;

public class MazeSerializer {

    private Maze maze;

    public MazeSerializer(Maze maze) {
        this.maze = maze;
    }

    public void saveMaze(File file) throws IOException {
        String fileName = file.getName();
        if (fileName.endsWith(".dat")) {
            saveMazeAsDat(file);
        } else if (fileName.endsWith(".map")) {
            saveMazeAsMap(file);
        } else if (fileName.endsWith(".num")) {
            saveMazeAsNum(file);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + fileName);
        }
    }

    public boolean loadMaze(File file) throws IOException {
        if (!file.exists() || file.isDirectory()) {
            System.err.println("File does not exist or is a directory: " + file.getAbsolutePath());
            return false;
        }

        String fileName = file.getName();
        if (fileName.endsWith(".dat")) {
            return loadMazeFromDat(file);
        } else if (fileName.endsWith(".map")) {
            loadMazeFromMap(file);
            return true;
        } else if (fileName.endsWith(".num")) {
            loadMazeFromNum(file);
            return true;
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + fileName);
        }

    }

    // Save Maze as Serialized Data (.dat)
    private void saveMazeAsDat(File file) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            serialize(out);
        }
        System.err.println("Maze saved as serialized file: " + file.getAbsolutePath());
    }

    // Load Maze from Serialized Data (.dat)
    private boolean loadMazeFromDat(File file) throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            return deserialize(in);
        }
    }

    public void serialize(FileOutputStream outStream) throws IOException {
        int dimension = maze.getDimension();
        int data = 0;
        int bitCount = 0;
        //Write the dimension of the maze out to the stream (width, height)
        ByteBuffer buffer = ByteBuffer.allocate(2 * Integer.BYTES);
        outStream.write(buffer.array());

        //Serialize the maze structure
        for (int row = 0; row < dimension; row++) {
            for (int column = 0; column < dimension; column++) {
                MazeNode currentNode = maze.at(row, column);
                //Bit flag for open down neighbor
                data = data << 1;
                if (currentNode.down != null) data |= 0x01;
                bitCount++;
                // Bit flag for right neighbor
                data = data << 1;
                if (currentNode.right != null) data |= 0x01;
                bitCount++;
                // Write byte to stream
                if (bitCount == Byte.SIZE){
                    outStream.write((byte) data);
                    data = bitCount = 0;
                }
            }
        }
        // Flush data to output stream
        if (bitCount != 0) {
            outStream.write((byte) (data << (Byte.SIZE - bitCount)));
        }
    }

    public boolean deserialize(FileInputStream inputStream) throws IOException {
        int dimension = maze.getDimension();
        final int EOF = -1;
        final int codewordSize = 2; // the bit size
        final int codewordBitmask = (0x3 << (Byte.SIZE - codewordSize));
        int received;

        //read dimension of maze from input stream
        byte[] proxy = new byte[2 * Integer.BYTES];
        received = inputStream.read(proxy);
        ByteBuffer buffer = ByteBuffer.wrap(proxy);
        //corrupted datafile - missing bytes
        if (received == EOF) {
            System.err.println( "Corrupted file detected: Incompatible file size: Aborting maze build" );
            return false;
        }
        int readWidth = buffer.getInt();
        int readHeight = buffer.getInt();
        System.err.println( "Loading dimensions: (" + readWidth + "," + readHeight + ")" );
        //width or height is not the same dimension as this maze object
        if (readWidth != dimension || readHeight != dimension) {
            System.err.println( "Incompatible dimensions read from file: Aborting maze build" );
            return false;
        }

        // Deserialize the maze structure
        int row = 0;
        int column = 0;
        maze.clearWalls();
        received = inputStream.read();
        while (received != EOF) {
            //reading 2-bit codewords. (1 codeword = 1 encoded maze node)
            for (int index = 0; index < Byte.SIZE/codewordSize; index ++) {
                if (row == dimension) {
                    // maze building is done - all cells visited
                    if( inputStream.available() > 1 ) {
                        System.err.println( "Corrupted file detected: Incompatible file size: Aborting maze build" );
                        maze.clear();
                        return false;
                    }
                    return true;
                }
                //build walls of node
                MazeNode currentNode = maze.at(row, column);
                int codeword = ((received & codewordBitmask) >>> (Byte.SIZE - codewordSize));
                deserializeNode(currentNode, codeword);
                received = received << codewordSize;
                column++;
                if (column == dimension) {
                    column = 0;
                    row++;
                }
            }
            received = inputStream.read();
        }
        return true;
    }

    public void deserializeNode(MazeNode node, int codeword) {
        final int downBitmask = 0x01 << 1;
        final int rightBitmask = 0x01;
        int dimension = maze.getDimension();
        if( (codeword & downBitmask) == 0 && node.row != dimension - 1 ) {
            /* add down wall */
            maze.addWall(maze.at(node.row + 1, node.column), maze.at(node.row, node.column));
        }
        if( (codeword & rightBitmask) == 0 && node.column != dimension - 1 ) {
            /* add right wall */
            maze.addWall(maze.at(node.row, node.column), maze.at(node.row, node.column + 1));
        }
    }

    public void saveMazeAsMap(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int dimension = maze.getDimension();
            for (int row = 0; row < dimension; row++) {
                for (int col = 0; col < dimension; col++) {
                    writer.write("+");
                    writer.write(maze.at(row, col).up != null ? "   " : "---");
                }
                writer.write("+\n");
                for (int col = 0; col < dimension; col++) {
                    writer.write(maze.at(row, col).left != null ? " " : "|");
                    writer.write("   ");
                }
                writer.write("|\n");
            }
            // Bottom boundary of the maze
            for (int col = 0; col < dimension; col++) {
                writer.write("+---");
            }
            writer.write("+\n");
        }
    }

    public void loadMazeFromMap(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        int dimension = (lines.size() - 1) / 2;
        maze = new Maze(dimension); //Reset the maze
        for (int row = 0; row < dimension; row++) {
            String topLine = lines.get(row * 2);
            // Adding horizontal walls
            for (int col = 0; col < dimension; col++) {
                if (topLine.charAt(col * 4 + 1) == '-') {
                    maze.addWall(maze.at(row, col), maze.at(row - 1, col));
                }
            }

            // Adding vertical walls
            String midLine = lines.get(row * 2 + 1);
            for (int col = 0; col < dimension; col++) {
                if (midLine.charAt(col * 4) == '|') {
                    maze.addWall(maze.at(row, col), maze.at(row, col - 1));
                }
            }
        }
    }

    public void saveMazeAsNum(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int dimension = maze.getDimension();
            for (int row = 0; row < dimension; row++) {
                for (int col = 0; col < dimension; col++) {
                    MazeNode node = maze.at(row, col);
                    int north = node.up == null ? 1 : 0;
                    int east = node.right == null ? 1 : 0;
                    int south = node.down == null ? 1 : 0;
                    int west = node.left == null ? 1 : 0;

                    writer.write(row + " " + col + " " + north + " " + east + " " + south + " " + west);
                    writer.newLine();
                }
            }
        }
    }

    public void loadMazeFromNum(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        int dimension = maze.getDimension();

        maze.clearWalls();

        for (String line : lines) {
            String[] parts = line.split(" ");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            int north = Integer.parseInt(parts[2]);
            int east = Integer.parseInt(parts[3]);
            int south = Integer.parseInt(parts[4]);
            int west = Integer.parseInt(parts[5]);

            MazeNode node = maze.at(row, col);

            if (north == 1 && row > 0) {
                maze.addWall(node, maze.at(row - 1, col));
            }
            if (east == 1 && col < dimension - 1) {
                maze.addWall(node, maze.at(row, col + 1));
            }
            if (south == 1 && row < dimension - 1) {
                maze.addWall(node, maze.at(row + 1, col));
            }
            if (west == 1 && col > 0) {
                maze.addWall(node, maze.at(row, col - 1));
            }
        }
    }
}
