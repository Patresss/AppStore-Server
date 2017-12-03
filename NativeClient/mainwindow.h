#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QGraphicsScene>

#include <QListWidget>
#include <QMainWindow>

#include "image1.h"
#include "game_info.h"


namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
    void add_item(QString item, QString icon_str, int id);

    std::vector<game_info> games;
private slots:

    void on_listWidget_itemClicked(QListWidgetItem *item);

private:
    void getGamesfromRest();
    int getGameSelectedId();
    int gameSelectedId;

    Ui::MainWindow *ui;
    std::vector<Image> images;

};

#endif // MAINWINDOW_H
