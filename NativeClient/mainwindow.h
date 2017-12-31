#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QGraphicsScene>

#include <QListWidget>
#include <QMainWindow>
#include "games_manager.h"

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    void on_listWidget_itemClicked(QListWidgetItem *item);
    void on_run_button_clicked();
    void on_remove_button_clicked();
    void on_update_button_clicked();
    void on_installGame_button_clicked();
    void on_gamesStore_listWidget_clicked(const QModelIndex &index);
    void on_tabWidget_currentChanged(int index);
    void on_run_Store_Button_clicked();

private:
    Ui::MainWindow *ui;
    games_manager *gm;
    int getGameSelectedId();

    void gamesStore_fillList();
    void gamesLocal_fillList();
    void checkInstalledGames();
};

#endif // MAINWINDOW_H
