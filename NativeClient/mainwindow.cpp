#include "mainwindow.h"
#include "ui_mainwindow.h"

#include <QMessageBox>
#include <QDebug>

#define TAB_STORE_ID 0
#define TAB_GAMES_ID 1

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setStyleSheet("background-color: black;");
    //this->setStyleSheet("background: transparent");
    QPalette palette = ui->game_name_label->palette();
    palette.setColor(ui->game_name_label->backgroundRole(), Qt::yellow);
    palette.setColor(ui->game_name_label->foregroundRole(), Qt::white);
    statusBar()->hide();
    ui->game_name_label->setPalette(palette);
    ui->game_version_label->setPalette(palette);
    ui->game_description_tb->setTextColor(Qt::white);

    ui->game_name_label_2->setPalette(palette);
    ui->game_version_label_2->setPalette(palette);
    ui->game_description_tb_2->setTextColor(Qt::white);

    ui->installGame_button->setStyleSheet("QPushButton {background-color: #232323; color: white;}");
    ui->run_button->setStyleSheet("QPushButton {background-color: #232323; color: white;}");
    ui->update_button->setStyleSheet("QPushButton {background-color: #232323; color: white;}");
    ui->remove_button->setStyleSheet("QPushButton {background-color: #232323; color: white;}");
    ui->update_games_bt->setStyleSheet("QPushButton {background-color: #232323; color: white;}");
    ui->run_Store_Button->setStyleSheet("QPushButton {background-color: #232323; color: white;}");

    ui->run_Store_Button->hide();
    ui->installGame_button->hide();

    gm = new games_manager();

    if(gm->some_updates_available)
    {
        ui->update_games_bt->show();
    }
    else
    {
        ui->update_games_bt->hide();
    }

    ui->run_button->hide();
    ui->update_button->hide();
    ui->remove_button->hide();

    gamesStore_fillList();

    QTimer::singleShot(1000, this, SLOT(timerHandler()));
}

MainWindow::~MainWindow()
{
    delete ui;
    gm->saveLocalInfo();
}

int MainWindow::getGameSelectedId()
{
    int id = 0;
    QItemSelectionModel* selectionModel;
    QAbstractListModel*  model;
    bool selected = false;

    if(ui->games_tab->isVisible()) // get id from list in games_tab
    {
        selectionModel = ui->listWidget->selectionModel();
        model          = qobject_cast<QAbstractListModel*>(ui->listWidget->model());
    }
    if(ui->store_tab->isVisible()) // get id from list in store_tab
    {
        selectionModel = ui->gamesStore_listWidget->selectionModel();
        model          = qobject_cast<QAbstractListModel*>(ui->gamesStore_listWidget->model());
    }

    if(model == 0)
    {
        QMessageBox::critical(this, "Error", "Wrong conversion");
    }

    for(int row = 0; row < model->rowCount(); ++row) // finding selected item;
    {
        selected = selectionModel->isSelected(model->index(row));
        if(selected == true)
        {
            id = row;
            break;
        }
    }

    return id;
}

void MainWindow::update_games()
{
    QMessageBox msgBox;
    bool status = false;

    status = gm->update_all_available();
    if(status == true)
    {
        msgBox.setInformativeText("updates complete");
        ui->update_games_bt->hide();
    }
    else
    {
        msgBox.setInformativeText("updates failed");
    }
    checkGameUpdateAvailable();
    gamesLocal_fillList();

    msgBox.setStandardButtons(QMessageBox::Ok);
    msgBox.exec();
}
void MainWindow::on_run_Store_Button_clicked()
{
    gm->gameStore_SelectedId = getGameSelectedId();
    gm->run_from_store();
}
void MainWindow::on_run_button_clicked()
{
    gm->run();
}

void MainWindow::on_remove_button_clicked()
{
    gm->remove();

    ui->game_image_2->setScene(NULL);
    ui->game_version_label_2->setText(NULL);
    ui->game_name_label_2->setText(NULL);
    ui->game_description_tb_2->setText(NULL);

    ui->run_button->hide();
    ui->update_button->hide();
    ui->remove_button->hide();
    gamesLocal_fillList();
}

void MainWindow::on_update_button_clicked()
{
    QSize image_size;
    QPixmap image;
    QGraphicsScene *scene;
    QString path;

    uint8_t tmp_id;

    tmp_id = gm->gameLocal_SelectedId = getGameSelectedId();
    gm->update(gm->gameLocal_SelectedId);
    checkGameUpdateAvailable();

    gm->gameLocal_SelectedId = getGameSelectedId();

    if(gm->gameLocal_SelectedId >= 0)
    {
        ui->run_button->show();
        ui->remove_button->show();
    }
    else
    {
        ui->run_button->hide();
        ui->remove_button->hide();
    }
    checkGameUpdateAvailable();

    path = "data/game" + QString::number(gm->gameLocal_SelectedId + 1) + "/image.png";
    image_size = ui->game_image->size();
    image_size.rwidth() -= 10;
    image_size.rheight() -= 10;

    image.load(gm->gamesLocal[gm->gameLocal_SelectedId].img_path);

    image = image.scaled(image_size);
    scene = new QGraphicsScene(this);
    scene->addPixmap(image);
    scene->setSceneRect(image.rect());

    ui->game_image_2->setScene(scene);

    ui->game_version_label_2->setText(gm->gamesLocal[gm->gameLocal_SelectedId].version);
    ui->game_name_label_2->setText(gm->gamesLocal[gm->gameLocal_SelectedId].name);
    ui->game_description_tb_2->setText(gm->gamesLocal[gm->gameLocal_SelectedId].description);

    gm->gameLocal_SelectedId = tmp_id;
    ui->listWidget->item(tmp_id)->setSelected(true);
    gamesLocal_fillList();
}

void MainWindow::on_installGame_button_clicked()
{
    QMessageBox msgBox;
    bool install_status;
    install_status = gm->install();
    if(install_status)
    {
        checkInstalledGames();
        msgBox.setInformativeText("Installation complete");
    }
    else
    {
        //installation failed, probably game is installed
        msgBox.setInformativeText("Installation failed");
    }

    msgBox.setStandardButtons(QMessageBox::Ok);
    msgBox.exec();
}

void MainWindow::on_gamesStore_listWidget_clicked(const QModelIndex &index)
{
    QSize image_size;
    QPixmap image;
    QGraphicsScene *scene;
    QString path;

    gm->gameStore_SelectedId = getGameSelectedId();
    checkInstalledGames();

    image_size = ui->game_image->size();
    image_size.rwidth() -= 10;
    image_size.rheight() -= 10;

    image = QPixmap::fromImage(gm->gamesStore[gm->gameStore_SelectedId].img);

    image = image.scaled(image_size);
    scene = new QGraphicsScene(this);
    scene->addPixmap(image);
    scene->setSceneRect(image.rect());

    ui->game_image->setScene(scene);

    ui->game_version_label->setText(gm->gamesStore[gm->gameStore_SelectedId].version);
    ui->game_name_label->setText(gm->gamesStore[gm->gameStore_SelectedId].name);
    ui->game_description_tb->setText(gm->gamesStore[gm->gameStore_SelectedId].description);
}

void MainWindow::on_listWidget_itemClicked(QListWidgetItem *item) //gameLocal_listWidget
{
    QSize image_size;
    QPixmap image;
    QGraphicsScene *scene;
    QString path;

    gm->gameLocal_SelectedId = getGameSelectedId();

    if(gm->gameLocal_SelectedId >= 0)
    {
        ui->run_button->show();
        ui->remove_button->show();
    }
    else
    {
        ui->run_button->hide();
        ui->remove_button->hide();
    }
    checkGameUpdateAvailable();

    path = "data/game" + QString::number(gm->gameLocal_SelectedId + 1) + "/image.png";
    image_size = ui->game_image->size();
    image_size.rwidth() -= 10;
    image_size.rheight() -= 10;

    image.load(gm->gamesLocal[gm->gameLocal_SelectedId].img_path);

    image = image.scaled(image_size);
    scene = new QGraphicsScene(this);
    scene->addPixmap(image);
    scene->setSceneRect(image.rect());

    ui->game_image_2->setScene(scene);

    ui->game_version_label_2->setText(gm->gamesLocal[gm->gameLocal_SelectedId].version);
    ui->game_name_label_2->setText(gm->gamesLocal[gm->gameLocal_SelectedId].name);
    ui->game_description_tb_2->setText(gm->gamesLocal[gm->gameLocal_SelectedId].description);

    checkGameUpdateAvailable();
}
void MainWindow::on_tabWidget_currentChanged(int index)
{
    switch(index)
    {
    case TAB_STORE_ID:
        gamesStore_fillList();
        checkInstalledGames();

        break;
    case TAB_GAMES_ID:
        gamesLocal_fillList();
        checkGameUpdateAvailable();
        break;
    }
}
void MainWindow::gamesStore_fillList()
{
    ui->gamesStore_listWidget->clear();
    for(uint32_t i = 0; i < gm->gamesStore.size(); i++)
    {
        ui->gamesStore_listWidget->addItem(new QListWidgetItem(QPixmap::fromImage(gm->gamesStore[i].icon), gm->gamesStore[i].name));
        ui->gamesStore_listWidget->item(i)->setTextColor(Qt::white);
    }
}
void MainWindow::gamesLocal_fillList()
{
    ui->listWidget->clear();
    for(uint32_t i = 0; i < gm->gamesLocal.size(); i++)
    {
        QPixmap icon;
        icon.load(gm->gamesLocal[i].icon_path);
        ui->listWidget->addItem(new QListWidgetItem(icon, gm->gamesLocal[i].name));
        ui->listWidget->item(i)->setTextColor(Qt::white);
    }
}
void MainWindow::checkInstalledGames() //TODO: refactor
{
    bool isInstalled = false;
    ui->run_Store_Button->hide();
    ui->installGame_button->hide();

    if(gm->gameStore_SelectedId >= 0)
    {
        for(uint32_t i = 0; i < gm->gamesLocal.size(); i++)
        {
            if(gm->gamesStore[gm->gameStore_SelectedId].id == gm->gamesLocal[i].id) // if game is installed
            {
                ui->run_Store_Button->show();
                isInstalled = true;
                break;
            }
        }
        if(!isInstalled)
        {
            ui->installGame_button->show();
        }
    }
}
bool MainWindow::checkGameUpdateAvailable()
{
    bool status = false;

    ui->update_games_bt->hide();
    for(unsigned int i = 0; i < gm->gamesLocal.size(); i++)
    {
        if(gm->gamesLocal[i].update_available)
        {
            ui->update_games_bt->show();
            status = true;
        }
    }

    ui->update_button->hide();
    if(gm->gameLocal_SelectedId >= 0)
    {
        if(gm->gamesLocal[gm->gameLocal_SelectedId].update_available)
        {
            ui->update_button->show();
        }
    }

    return status;
}

void MainWindow::on_update_games_bt_clicked()
{
    update_games();
}

void MainWindow::timerHandler()
{
    if(checkGameUpdateAvailable())
    {
        QMessageBox msgBox;
        msgBox.setText("updates available");
        msgBox.setInformativeText("update all old games?");
        msgBox.setStandardButtons(QMessageBox::Yes | QMessageBox::No);
        msgBox.setModal(false);
        if(msgBox.exec() == QMessageBox::Yes)
        {
            update_games();
        }
    }
}
