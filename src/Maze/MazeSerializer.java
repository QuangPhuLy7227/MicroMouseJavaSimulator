package Maze;

import java.io.*;
import java.nio.ByteBuffer;

public class MazeSerializer {
    public void saveMaze(Maze maze, File datafile) {
        FileOutputStream out = null;
        long prevMillis = System.currentTimeMillis();
        System.err.println( "Saving Maze..." );
        try {
            out = new FileOutputStream(datafile);
            serialize(maze, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println( "Time taken to save maze: " + (System.currentTimeMillis() - prevMillis) / 1000.0 + " sec" );
    }

    public boolean loadMaze(Maze maze, File datafile) {
        if (!datafile.exists() || datafile.isDirectory()) {
            //datafile does not exist
            return false;
        }
        FileInputStream in = null;
        boolean status = false;
        long prevMillis = System.currentTimeMillis();
        System.err.println( "Loading Maze..." );
        try {
            in = new FileInputStream( datafile );
            status = deserialize(maze, in);
        }
        catch( IOException e ) {
            e.printStackTrace();
            status = false;
        }
        finally {
            try {
                if( in != null ) {
                    in.close();
                }
            }
            catch( IOException e ) {
                e.printStackTrace();
                status = false;
            }
        }

        if( status == true ) {
            System.err.println( "Time taken to load maze: " + (System.currentTimeMillis() - prevMillis) / 1000.0 + " sec" );
        }
        else {
            System.err.println( "Unsuccessful maze load." );
        }

        return status;
    }

    public void serialize(Maze maze, FileOutputStream outstream) throws IOException {
        int dimension = maze.getDimension();
        int data = 0;
        int bitCount = 0;
        //Write the dimension of the maze out to the stream (width, height)
        ByteBuffer buffer = ByteBuffer.allocate(2 * Integer.BYTES);
        outstream.write(buffer.array());

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
                if (currentNode.right != null) data |= 1;
                bitCount++;
                // Write byte to stream
                if (bitCount == Byte.SIZE){
                    outstream.write((byte) data);
                    data = bitCount = 0;
                }
            }
        }
        // Flush data to outstream
        if (bitCount != 0) {
            outstream.write((byte) (data << (Byte.SIZE - bitCount)));
        }
    }

    public boolean deserialize(Maze maze, FileInputStream instream) throws IOException {
        int dimension = maze.getDimension();
        final int EOF = -1;
        final int codewordSize = 2; //bit size
        final int codewordBitmask = (0x3 << (Byte.SIZE - codewordSize));
        int received;

        //read dimension of maze from input stream
        byte[] proxy = new byte[2 * Integer.BYTES];
        received = instream.read(proxy);
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
        received = instream.read();
        while (received != EOF) {
            //reading 2-bit codewords. (1 codeword = 1 encoded maze node)
            for (int index = 0; index < Byte.SIZE/codewordSize; index ++) {
                if (row == dimension) {
                    // maze building is done - all cells visited
                    if( instream.available() > 1 ) {
                        System.err.println( "Corrupted file detected: Incompatible file size: Aborting maze build" );
                        maze.clear();
                        return false;
                    }
                    return true;
                }
                //build walls of node
                MazeNode currentNode = maze.at(row, column);
                int codeword = ((received & codewordBitmask) >>> (Byte.SIZE - codewordSize));
                deserializeNode(maze, currentNode, codeword);
                received = received << codewordSize;
                column++;
                if (column == dimension) {
                    column = 0;
                    row++;
                }
            }
            received = instream.read();
        }
        return true;
    }

    private void deserializeNode(Maze maze, MazeNode node, int codeword) {
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
}
