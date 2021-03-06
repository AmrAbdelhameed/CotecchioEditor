package Export;

import Data.Player;
import Interface.UserController;

import javax.swing.*;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;


class ExportXlsLeaderboard {
   private UserController ui;

   ExportXlsLeaderboard(UserController ui, ArrayList<Player> topPlayers) {
      this.ui = ui;

      export(topPlayers);
   }

   private void export(ArrayList<Player> topPlayers) {
      String paper = getPaper(topPlayers);

      String path = getFile();

      if (path != null) {
         try {
            write(path, paper);
         } catch (FileNotFoundException e3) {
            int choice = JOptionPane.showConfirmDialog(ui, ui.getSettings().getResourceBundle().getString("askOverwrite"),
                    ui.getSettings().getResourceBundle().getString("overwrite"), JOptionPane.DEFAULT_OPTION);

            if (choice == JOptionPane.OK_OPTION) {
               try {
                  write(path, paper);
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
            }
         } catch (IOException e1) {
            JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("errorWritingFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
         }
      } else {
         try {
            throw new IOException();
         } catch (IOException e1) {
            JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("errorBufferingFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
         }
      }
   }

   private String getFile() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setAcceptAllFileFilterUsed(false);
      fileChooser.addChoosableFileFilter(new XlsFilter());

      int res = fileChooser.showSaveDialog(ui);

      if (res == JFileChooser.APPROVE_OPTION) {
         return fileChooser.getSelectedFile().getPath() + ".xls";
      } else {
         JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("errorLoadingFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
      }

      return null;
   }

   private String getPaper(ArrayList<Player> players) {
      StringBuilder paper = new StringBuilder();
      DecimalFormat df = new DecimalFormat();
      df.setRoundingMode(RoundingMode.DOWN);

      paper.append("Graduatoria Cotecchio\n\n");
      paper.append("Nome Cognome\tPunteggio\tPartite\tW-L\tMedia\tPelliccioni\tMedia P.\tCappotti\tMedia C.\n");

      for (Player p : players) {
         paper.append(p.getName()).append("\t")
                 .append(p.getScore()).append("\t")
                 .append(p.getTotalPlays()).append("\t")
                 .append(p.getTotalWins()).append("w-").append(p.getTotalPlays() - p.getTotalWins()).append("l\t")
                 .append(df.format(p.getScore() / (float) p.getTotalPlays())).append("\t")
                 .append(p.getPelliccions()).append("\t")
                 .append(df.format(p.getPelliccions() / (float) p.getTotalPlays())).append("\t")
                 .append(p.getCappottens()).append("\t")
                 .append(df.format(p.getCappottens() / (float) p.getTotalPlays()));
         paper.append("\n");
      }

      return paper.toString();
   }

   private void write(String path, String paper) throws IOException{
      PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
      out.write(paper);
      out.close();
   }
}
