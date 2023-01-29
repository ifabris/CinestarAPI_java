/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Film;
import hr.algebra.model.FilmArchive;
import hr.algebra.model.Genre;
import hr.algebra.rss.Parser;
import hr.algebra.utils.JAXBUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import hr.algebra.utils.MessageUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Ivan
 */
public class Admin_Site extends javax.swing.JFrame {

    private DefaultListModel<Film> moviesModel;
    private Repository repository;
    private static final String FILENAME = "filmsarchive.xml";

    /**
     * Creates new form Admin_Site
     */
    public Admin_Site() {
        initComponents();
        init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jList = new javax.swing.JScrollPane();
        lsMovies = new javax.swing.JList<>();
        btnUpload = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Movies :");

        jList.setViewportView(lsMovies);

        btnUpload.setText("Upload");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jList)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(396, 396, 396)
                        .addComponent(btnUpload)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 366, Short.MAX_VALUE)
                        .addComponent(btnDelete)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jList, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpload)
                    .addComponent(btnDelete))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        // TODO add your handling code here:
        try {
            List<Film> movies = Parser.parse();
            List<Director> directors = new ArrayList<>();
            List<Actor> actors = new ArrayList<>();
            List<Genre> genres = new ArrayList<>();
            repository.createMovies(movies);
            List<Film> filmsWithActors = new ArrayList<>();
            
            for (Film movie : movies) {
                if (movie.getActors()!=null) {
                    filmsWithActors.add(movie);
                }
            }
            
            
            for (Film movie : filmsWithActors) {
                directors.addAll(movie.getDirector());
                genres.addAll(movie.getGenre());
                try {
                    
                     actors.addAll(movie.getActors());               
                } catch (Exception e) {
                }
            }
            List<Film> movieWithoutNull = new ArrayList<>();
            
            
            for (Film movie : filmsWithActors) {
                if (movie.getActors()!=null) {
                    movieWithoutNull.add(movie);
                }
            }

            List<Actor> distinctElements = actors.stream()
                    .filter(distinctByKey(p -> p.getFirstName()))
                    .collect(Collectors.toList());

            repository.createDirectors(directors);
            repository.createActors(actors);
            repository.createGenres(genres);

            List<Film> filmsFromSQL = repository.selectMovies();
            List<Director> dirsFromSQL = repository.selectDirectors();
            List<Actor> actorsFromSQL = repository.selectActors();
            List<Genre> genresFromSQL = repository.selectGenres();

            repository.createMoviesDirectors(movies, directors, dirsFromSQL, filmsFromSQL);

            repository.createMovieGenre(filmsWithActors, genres, filmsFromSQL, genresFromSQL);

            repository.createMovieActors(movieWithoutNull, distinctElements, actorsFromSQL, filmsFromSQL);
            
            initXML(filmsFromSQL);
    
            loadModel();
        } catch (Exception ex) {
            MessageUtils.showErrorMessage("Unrecoverable error", "Unable to upload movies");
            System.exit(1);
        }
    }//GEN-LAST:event_btnUploadActionPerformed

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            repository.deleteFilmGenre();
            repository.deleteFilmDjelatnik();
            repository.deleteMovies();
            loadModel();
        } catch (Exception ex) {
            MessageUtils.showErrorMessage("Unrecoverable error", "Unable to delete movies");
            System.exit(1);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin_Site.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin_Site.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin_Site.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin_Site.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin_Site().setVisible(true);
            }
        });
    }

    private void init() {
        try {
            repository = RepositoryFactory.getRepository();
            moviesModel = new DefaultListModel<>();
            loadModel();
        } catch (Exception ex) {
            Logger.getLogger(Admin_Site.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Unrecoverable error", "Cannot initiate the form");
            System.exit(1);
        }
    }

    private void loadModel() throws Exception {
        List<Film> filmovi = repository.selectMovies();
        moviesModel.clear();
        filmovi.forEach(movie -> moviesModel.addElement(movie));
        lsMovies.setModel(moviesModel);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jList;
    private javax.swing.JList<Film> lsMovies;
    // End of variables declaration//GEN-END:variables


    private void initXML(List<Film> movies) {
         List<Film> xmlFilms = new ArrayList<>();
             if (movies != null) {
                    movies.forEach(a -> xmlFilms.add(a));
              }
            
            try {
            JAXBUtils.save(new FilmArchive(xmlFilms),FILENAME);
            MessageUtils.showInformationMessage("Info", "Films saved");
        } catch (JAXBException ex) {
            MessageUtils.showErrorMessage("Error", "Unable to save films");
            Logger.getLogger(Admin_Site.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}