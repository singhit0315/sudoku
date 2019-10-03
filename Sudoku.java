/*
 * Name: Amarjit Singh
 * Course : 340
 * Assignment: write  a recursive  program  to solve  Sudoku  puzzle
 *
 *
 */
//package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;


public class Sudoku extends javax.swing.JFrame {

    // array to store the puzzle
    private int[][] puzzle;

    // solution of the puzzle
    private int[][] solution;

    // number of row of the puzzle
    private int rows;

    // number of columns of the puzzle
    private int cols;

    // array of Label's to display the puzzle
    private JLabel[][] gridLabels;

    // name of the input puzzle file
    private String puzzleSolutionFilename;

    // font and size
    private Font font = new Font("Arial", Font.PLAIN, 16);


    // Default class constructor for empty Sudoku puzzle.
    public Sudoku() {
        initComponents();
    }


    //Class constructor that creates the Sudoku puzzle from a given file
    // display the puzzle on GUI.


    public Sudoku(String filename){
        initComponents();
        loadPuzzle(filename);
        displayPuzzle();
    }


    // Read a puzzle from a file
    // display it in the GUI  window.
    private void loadPuzzle(String filename){
        puzzleSolutionFilename = filename + ".solved";
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            String line = fileScanner.nextLine();
            String[] data = line.split(" ");
            cols = data.length;
            List<String> lines = new ArrayList<String>();
            lines.add(line);
            while(fileScanner.hasNextLine()){
                lines.add(fileScanner.nextLine());
            }
            fileScanner.close();
            rows = lines.size();
            puzzle = new int[rows][cols];
            solution = new int[rows][cols];
            if(gridLabels == null)
                gridLabels = new JLabel[rows][cols];
            jPanelCenter.setLayout(new GridLayout(rows, cols));
            for(int i=0; i < rows; i++){
                line = lines.get(i);
                data = line.split(" ");
                for(int j=0; j < cols; j++){
                    int value = Integer.parseInt(data[j]);
                    puzzle[i][j] = value;
                    if(gridLabels[i][j] == null)
                        gridLabels[i][j] = new JLabel();
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            System.exit(0);
        }
    }


    // Display the puzzle stored in the array in the GUI.

    private void displayPuzzle() {
        jPanelCenter.removeAll();
        //gridLabels = new JLabel[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int value = puzzle[i][j];
                if (value > 0) {
                    gridLabels[i][j] = new JLabel(value + "");
                    gridLabels[i][j].setForeground(Color.red);
                } else {
                    gridLabels[i][j] = new JLabel("");
                    gridLabels[i][j].setForeground(Color.LIGHT_GRAY);
                }
                gridLabels[i][j].setBorder(new LineBorder(Color.BLACK));
                gridLabels[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
                gridLabels[i][j].setHorizontalAlignment(JLabel.CENTER);
                gridLabels[i][j].setFont(font);
                jPanelCenter.add(gridLabels[i][j]);
            }
        }
        jPanelCenter.revalidate();
    }


    //Recursive method to solve using DFS algorithm.
    //cellNo the starting cell number (between 0 - 80) to solve from
    //return true if puzzle solved, else return false

    private boolean solveDFS(int cellNo){
        //System.out.println(cellNo);
        if(cellNo >= 81)
            return true;
        int i = cellNo/9;
        int j = cellNo - (i*9);
        if(puzzle[i][j] > 0){
            return solveDFS(cellNo + 1);
        }
        else{
            for(int value=1; value <= 9; value++){
                int[] pos = getSubGridStartPos(i, j);
                if(isValidAtRow(i, value) && isValidAtColumn(j, value) && isValidAtSubGrid(pos[0], pos[1], value)){
                    //System.out.println(value + " valid at " + i+","+j);
                    puzzle[i][j] = value;
                    gridLabels[i][j].setText(value+"");
                    jPanelCenter.revalidate();
                    if((i == (rows-1)) && (j == (cols-1)))
                        return true;

                    if(solveDFS(cellNo + 1))
                        return true;
                    puzzle[i][j] = 0;
                    gridLabels[i][j].setText("");
                    jPanelCenter.revalidate();
                }
            }
            return false;
        }
    }


    // Get the first row and column of the puzzle sub grid that contains the position (i, j)
    // i row position in the grid
    // j column position in the grid
    // return a two element array of starting row and column of the sub grid

    private int[] getSubGridStartPos(int i, int j){
        int[] pos = new int[2];
        pos[0] = (i / 3)*3;
        pos[1] = (j / 3)*3;
        return pos;
    }



    // Check whether a given value violates the sudoku game rule in a row.
    // row a row in the puzzle grid
    // value the input value
    // return true if value can be assigned to puzzle grid at row, else false

    private boolean isValidAtRow(int row, int value){
        for(int j=0; j < cols; j++){
            if(puzzle[row][j] == value)
                return false;
        }
        return true;
    }


    //Check whether a given value violates the sudoku game rule in a column.
    // col a column in the puzzle grid
    // value the input value
    //return true if value can be assigned to puzzle grid at column, else false

    private boolean isValidAtColumn(int col, int value){
        for(int i=0; i < rows; i++){
            if(puzzle[i][col] == value)
                return false;
        }
        return true;
    }


    //Check whether a given value violates the sudoku game rule in a 3x3 sub grid.

    // gridStartRow the first row in the puzzle sub grid
    // gridStartCol  the first column in the puzzle sub grid
    // value the input value
    //return true if value can be assigned to puzzle sub grid, else false

    private boolean isValidAtSubGrid(int gridStartRow, int gridStartCol, int value){
        for(int i=0; i < 3; i++){
            for(int j=0; j < 3; j++){
                if(puzzle[i+gridStartRow][j+gridStartCol] == value)
                    return false;
            }
        }
        return true;
    }




    // This method is called from within the constructor to initialize the form.
     /* WARNING: Do NOT modify this code. The content of this method is always
        regenerated by the Form Editor.
     **/
    @SuppressWarnings("unchecked")

    private void initComponents() {

        jPanelCenter = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        btnChooseFile = new javax.swing.JButton();
        btnSolve = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sudoku Solver");
        getContentPane().setLayout(new java.awt.BorderLayout(0, 10));

        jPanelCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanelCenter.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout jPanelCenterLayout = new javax.swing.GroupLayout(jPanelCenter);
        jPanelCenter.setLayout(jPanelCenterLayout);
        jPanelCenterLayout.setHorizontalGroup(
                jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 360, Short.MAX_VALUE)
        );
        jPanelCenterLayout.setVerticalGroup(
                jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 211, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelCenter, java.awt.BorderLayout.CENTER);

        jPanelButtons.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jPanelButtonsAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        btnChooseFile.setText("Choose Sudoku File...");
        btnChooseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseFileActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnChooseFile);

        btnSolve.setText("Solve");
        btnSolve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSolveActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnSolve);

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnExit);

        getContentPane().add(jPanelButtons, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void jPanelButtonsAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jPanelButtonsAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanelButtonsAncestorAdded

    private void btnChooseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseFileActionPerformed
        // TODO add your handling code here:
        JFileChooser fch = new JFileChooser(".");
        if( fch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            File f = fch.getSelectedFile();
            loadPuzzle(f.getAbsolutePath());
            displayPuzzle();
        }
    }//GEN-LAST:event_btnChooseFileActionPerformed

    private void btnSolveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSolveActionPerformed
        // TODO add your handling code here:
        int startCellNo = 0;
        if(solveDFS(startCellNo)){
            JOptionPane.showMessageDialog(this, "Sudoku SOLVED.");
            writeSudokuSolutionToFile(puzzleSolutionFilename);
        }
        else{
            JOptionPane.showMessageDialog(this, "Sudoku NOT solvable!");
        }
    }//GEN-LAST:event_btnSolveActionPerformed

    /**
     * Save the puzzle solution to an output file.
     *
     * @param filename the path of the output file
     */
    private void writeSudokuSolutionToFile(String filename){
        try {
            PrintWriter pw = new PrintWriter(new File(filename));
            for(int i=0; i < rows; i++){
                for(int j=0; j < cols; j++){
                    pw.printf("%d ", puzzle[i][j]);
                }
                pw.println();
            }
            pw.close();
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Sudoku sudoku;

        if(args.length > 0){
            sudoku = new Sudoku(args[0]);
        }
        else{
            sudoku = new Sudoku();
        }
        sudoku.setDefaultCloseOperation(EXIT_ON_CLOSE);
        sudoku.setLocationRelativeTo(null);
        sudoku.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooseFile;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnSolve;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelCenter;
    // End of variables declaration//GEN-END:variables
}

