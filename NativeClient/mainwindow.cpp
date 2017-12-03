#include "mainwindow.h"
#include "ui_mainwindow.h"

#include <QDirIterator>
#include <QMessageBox>

#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonArray>
#include <QJsonValue>
#include "echoclient.h"
#include "restclient_inc/restclient.h"

#include <QImage>
#include <QByteArray>
#include <QTextStream>
#include <QDebug>
#include <QLabel>


MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    this->setStyleSheet("background-color: black;");
    QPalette palette = ui->game_name_label->palette();
    palette.setColor(ui->game_name_label->backgroundRole(), Qt::yellow);
    palette.setColor(ui->game_name_label->foregroundRole(), Qt::white);

    ui->game_name_label->setPalette(palette);
    ui->game_version_label->setPalette(palette);

    ui->game_description_tb->setTextColor(Qt::white);


    gameSelectedId = -1;
    getGamesfromRest();

}

MainWindow::~MainWindow()
{
    delete ui;
}
void MainWindow::add_item(QString item, QString icon_str, int id)
{
    ui->listWidget->addItem(new QListWidgetItem(QIcon(icon_str), item));
    ui->listWidget->item(id)->setTextColor(Qt::white);
}

void MainWindow::on_listWidget_itemClicked(QListWidgetItem *item)
{
    gameSelectedId = getGameSelectedId();

    QSize image_size;
    QPixmap image;
    QGraphicsScene *scene;
    QString path;

    path = "data/game" + QString::number(gameSelectedId + 1) + "/image.png";
    image_size = ui->game_image->size();
    image_size.rwidth() -= 10;
    image_size.rheight() -= 10;

    image.load(path);

    image = image.scaled(image_size);
    scene = new QGraphicsScene(this);
    scene->addPixmap(image);
    scene->setSceneRect(image.rect());

    ui->game_image->setScene(scene);

    //images.push_back(image);

    //ui->game_image->setScene(images[1].scene);

/////////////////////////////////////////////////////

    ui->game_version_label->setText(games[gameSelectedId].version);
    ui->game_name_label->setText(games[gameSelectedId].name);
    ui->game_description_tb->setText(games[gameSelectedId].description);

}
void MainWindow::getGamesfromRest()
{
   //RestClient::Response r = RestClient::get("http://localhost:8080/api/welcome/Tom");
   RestClient::Response f = RestClient::get("http://localhost:8080/api/games");
   RestClient::Response g = RestClient::get("http://localhost:8080/api");
   //RestClient::Response g2 = RestClient::get("http://localhost:8080/adpi");
   //RestClient::Response g3 = RestClient::get("http://localhost:8089/adpi");
   //qDebug() << QString::fromUtf8(r.body.c_str());
   //qDebug() << QString::fromUtf8(f.body.c_str());
   QString GamesJSON = QString::fromUtf8(f.body.c_str());

   QJsonDocument jsonResponse = QJsonDocument::fromJson(GamesJSON.toUtf8());
   QJsonArray jsonArray = jsonResponse.array();

   jsonArray.count();

   if(!QDir("data").exists())
   {
          QDir().mkdir("data");
   }

   for(int i=0; i<jsonArray.count(); i++)
   {
       QString id = QString::number(jsonArray.at(i).toObject().value("id").toInt());
       QString name = jsonArray.at(i).toObject().value("name").toString();
       QString desc = jsonArray.at(i).toObject().value("description").toString();
       QString v = jsonArray.at(i).toObject().value("version").toString();
       QString icon = jsonArray.at(i).toObject().value("icon").toString();
       QString image = jsonArray.at(i).toObject().value("image").toString();
       qDebug() << id + " " + name + " " + desc + " " + v;

       if(!QDir("data\\game" + id).exists()) //create folder for game if is not exist
       {
              QDir().mkdir("data\\game" + id);
       }

       {// icon
           QString icon_file = "data\\game" + id + "\\icon.png";
           QByteArray base64Data = icon.toUtf8().left(4967296);
           QImage icon_image;
           icon_image.loadFromData(QByteArray::fromBase64(base64Data), "PNG");
           //QLabel label(0);
           //label.setPixmap(QPixmap::fromImage(image));
           //label.show();
           icon_image.save(icon_file, "PNG");
       }

       { //image
           QString image_file = "data\\game" + id + "\\image.png";
           QByteArray base64Data = image.toUtf8().left(4967296);
           QImage image_image;
           image_image.loadFromData(QByteArray::fromBase64(base64Data), "PNG");
           //QLabel label(0);
           //label.setPixmap(QPixmap::fromImage(image));
           //label.show();
           image_image.save(image_file, "PNG");
       }
       games.push_back(game_info(i, name, v, desc));
       add_item(name,"data\\game" + id + "\\icon.png", i);
   }
}
int MainWindow::getGameSelectedId()
{
    int id;
    QItemSelectionModel* selectionModel = ui->listWidget->selectionModel();
    QAbstractListModel*  model          = qobject_cast<QAbstractListModel*>(ui->listWidget->model());
    bool selected = false;

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
