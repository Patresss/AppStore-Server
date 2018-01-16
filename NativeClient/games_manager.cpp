#include <QMessageBox>
#include <QDirIterator>
#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonArray>
#include <QJsonValue>
#include <QDebug>
#include <QProcess>

#include "games_manager.h"
#include "restclient_inc/restclient.h"
games_manager::games_manager()
{
    gameStore_SelectedId = -1;
    gameLocal_SelectedId = -1;
    getGamesfromRest();
    readLocalInfo();
    some_updates_available = false;
    checkUpdates();
}

void games_manager::run()
{
    gamesLocal[gameLocal_SelectedId].run();
}

void games_manager::run_from_store()
{
    if(gameStore_SelectedId >= 0)
    {
        for(uint32_t i = 0; i < gamesLocal.size(); i++)
        {
            if(gamesStore[gameStore_SelectedId].id == gamesLocal[i].id)
            {
                gamesLocal[i].run();
                break;
            }
        }
    }
}

bool games_manager::install()
{
    bool status;
    std::string rest_id = QString::number(gamesStore[gameStore_SelectedId].id).toUtf8().constData();
    RestClient::Response f = RestClient::get("http://localhost:8080/api/games/" + rest_id);
    QString GamesJSON = QString::fromUtf8(f.body.c_str());
    QJsonDocument jsonResponse = QJsonDocument::fromJson(GamesJSON.toUtf8());
    QJsonObject jsonObject = jsonResponse.object();

    if(!QDir("data").exists())
    {
           QDir().mkdir("data");
    }

    QString id = QString::number(jsonObject["id"].toInt());
    QString name = jsonObject["name"].toString();
    QString desc = jsonObject["description"].toString();
    QString v = jsonObject["version"].toString();
    QString icon = jsonObject["icon"].toString();
    QString image = jsonObject["image"].toString();
    QString file = jsonObject["file"].toString();

    if(!QDir("data\\game" + id).exists()) //create folder for game if is not exist
    {
        QDir().mkdir("data\\game" + id);

        QImage icon_image;
        QString icon_path;
        {// icon
            icon_path = "data\\game" + id + "\\icon.png";
            QByteArray base64Data = icon.toUtf8().left(4967296);
            icon_image.loadFromData(QByteArray::fromBase64(base64Data), "PNG");
            icon_image.save(icon_path, "PNG");
        }

        QImage image_image;
        QString image_path;
        { //image
            image_path = "data\\game" + id + "\\image.png";
            QByteArray base64Data = image.toUtf8().left(4967296);
            image_image.loadFromData(QByteArray::fromBase64(base64Data), "PNG");
            image_image.save(image_path, "PNG");
        }

        QString game_path;
        { //game_path
            game_path = "data\\game" + id + "\\game.nes";
            QByteArray base64Data = file.toUtf8().left(4967296);

            QFile file(game_path);
            if (file.open(QIODevice::ReadWrite))
            {
               file.write(QByteArray::fromBase64(base64Data));
            }
            file.close();
        }
        gamesLocal.push_back(gameLocal(id.toInt(), name, v, desc, game_path, icon_path, image_path));
        status = true;
    }
    else
    {
        status = false;
    }
    return status;
}

void games_manager::remove()
{
    bool result;
    result = removeDir("data\\game"+ QString::number(gamesLocal[gameLocal_SelectedId].id));
    if(result)
    {
        //msg box OK
    }
    else
    {
       //msg box failed to remove from local
    }

    gamesLocal.erase(gamesLocal.begin() + gameLocal_SelectedId);
    gameLocal_SelectedId = -1;
}

bool games_manager::update(int local_id)
{
    bool status;
    removeDir("data\\game"+ QString::number(gamesLocal[local_id].id));

    std::string rest_id = QString::number(gamesLocal[local_id].id).toUtf8().constData();
    RestClient::Response f = RestClient::get("http://localhost:8080/api/games/" + rest_id);
    QString GamesJSON = QString::fromUtf8(f.body.c_str());
    QJsonDocument jsonResponse = QJsonDocument::fromJson(GamesJSON.toUtf8());
    QJsonObject jsonObject = jsonResponse.object();

    if(!QDir("data").exists())
    {
           QDir().mkdir("data");
    }

    QString id = QString::number(jsonObject["id"].toInt());
    QString name = jsonObject["name"].toString();
    QString desc = jsonObject["description"].toString();
    QString v = jsonObject["version"].toString();
    QString icon = jsonObject["icon"].toString();
    QString image = jsonObject["image"].toString();
    QString file = jsonObject["file"].toString();

    if(!QDir("data\\game" + id).exists()) //create folder for game if is not exist
    {
        QDir().mkdir("data\\game" + id);

        QImage icon_image;
        QString icon_path;
        {// icon
            icon_path = "data\\game" + id + "\\icon.png";
            QByteArray base64Data = icon.toUtf8().left(4967296);
            icon_image.loadFromData(QByteArray::fromBase64(base64Data), "PNG");
            icon_image.save(icon_path, "PNG");
        }

        QImage image_image;
        QString image_path;
        { //image
            image_path = "data\\game" + id + "\\image.png";
            QByteArray base64Data = image.toUtf8().left(4967296);
            image_image.loadFromData(QByteArray::fromBase64(base64Data), "PNG");
            image_image.save(image_path, "PNG");
        }

        QString game_path;
        { //game_path
            game_path = "data\\game" + id + "\\game.nes";
            QByteArray base64Data = file.toUtf8().left(4967296);

            QFile file(game_path);
            if (file.open(QIODevice::ReadWrite))
            {
               file.write(QByteArray::fromBase64(base64Data));
            }
            file.close();
        }
        gamesLocal[local_id].id = id.toInt();
        gamesLocal[local_id].name = name;
        gamesLocal[local_id].version = v;
        gamesLocal[local_id].description = desc;
        gamesLocal[local_id].game_path = game_path;
        gamesLocal[local_id].icon_path = icon_path;
        gamesLocal[local_id].img_path = image_path;

        gamesLocal[local_id].update_available = false;

        status = true;
    }
    else
    {
        status = false;
    }

    return status;
}

bool games_manager::update_all_available()
{
    bool status = false
            ;
    for (unsigned int i = 0; i < gamesLocal.size(); i++)
    {
        if(gamesLocal[i].update_available)
        {
            status = update(i);
            if(status == false)
            {
                break;
            }
        }
    }

    return status;
}

void games_manager::saveLocalInfo()
{
    //gamesLocal JSON to file
    QJsonDocument doc;
    QJsonArray jsonArray;

    QJsonValue id;
    QJsonValue name;
    QJsonValue description;
    QJsonValue version;
    QJsonValue icon_path;
    QJsonValue image_path;
    QJsonValue game_path;

    QJsonObject json_game_obj;

    for(uint32_t i = 0; i < gamesLocal.size(); i++)
    {
        id = gamesLocal[i].id;
        name = gamesLocal[i].name;
        description = gamesLocal[i].description;
        version = gamesLocal[i].version;
        icon_path = gamesLocal[i].icon_path;
        image_path = gamesLocal[i].img_path;
        game_path = gamesLocal[i].game_path;

        json_game_obj["id"] = id;
        json_game_obj["name"] = name;
        json_game_obj["description"] = description;
        json_game_obj["version"] = version;
        json_game_obj["icon_path"] = icon_path;
        json_game_obj["image_path"] = image_path;
        json_game_obj["game_path"] = game_path;

        jsonArray.append(json_game_obj);
    }
    doc.setArray(jsonArray);
    QString game_info_db(doc.toJson());
    removeDir("data\\game_info.db");
    QFile file("data\\game_info.db");
    if (file.open(QIODevice::ReadWrite | QIODevice::Truncate))
    {
       file.write(game_info_db.toUtf8());
    }
    file.close();
}

void games_manager::readLocalInfo()
{
    //from JSON file to gamesLocal
    QString game_info_db;
    QFile file1("data\\game_info.db");
    if(!file1.exists())
    {
        return; //no data to read;
    }
    file1.open(QIODevice::ReadOnly | QIODevice::Text);
    QTextStream s1(&file1);
    s1.setCodec("UTF-8");
    game_info_db.append(s1.readAll());
    file1.close();

    QString GamesJSON = QString::fromUtf8(game_info_db.toUtf8());
    QJsonDocument jsonResponse = QJsonDocument::fromJson(GamesJSON.toUtf8());
    QJsonArray jsonArray = jsonResponse.array();
    jsonArray.count();

    for(int i = 0; i < jsonArray.count(); i++)
    {
        QString id = QString::number(jsonArray.at(i).toObject().value("id").toInt());
        QString name = jsonArray.at(i).toObject().value("name").toString();
        QString description = jsonArray.at(i).toObject().value("description").toString();
        QString version = jsonArray.at(i).toObject().value("version").toString();
        QString icon_path = jsonArray.at(i).toObject().value("icon_path").toString();
        QString image_path = jsonArray.at(i).toObject().value("image_path").toString();
        QString game_path = jsonArray.at(i).toObject().value("game_path").toString();

        //load data from file to ram
        gamesLocal.push_back(gameLocal(id.toInt(), name, version, description, game_path, icon_path, image_path));
    }
}

bool games_manager::removeDir(const QString &dirName)
{
    bool result = true;
    QDir dir(dirName);
    result = dir.removeRecursively();
    QString qstr = "RD /S /Q " + dirName;
    const char *c = qstr.toLatin1().data();
    system(c);

    return result;
}

void games_manager::getGamesfromRest()
{
   RestClient::Response f = RestClient::get("http://localhost:8080/api/games");
   QString GamesJSON = QString::fromUtf8(f.body.c_str());

   QJsonDocument jsonResponse = QJsonDocument::fromJson(GamesJSON.toUtf8());
   QJsonArray jsonArray = jsonResponse.array();
   jsonArray.count();

   if(!QDir("data").exists())
   {
          QDir().mkdir("data");
   }

   for(int i = 0; i < jsonArray.count(); i++)
   {
       QString id = QString::number(jsonArray.at(i).toObject().value("id").toInt());
       QString name = jsonArray.at(i).toObject().value("name").toString();
       QString desc = jsonArray.at(i).toObject().value("description").toString();
       QString v = jsonArray.at(i).toObject().value("version").toString();
       QString icon = jsonArray.at(i).toObject().value("icon").toString();
       QString image = jsonArray.at(i).toObject().value("image").toString();
       qDebug() << id + " " + name + " " + desc + " " + v;

       QImage icon_image;
       {// icon
           QByteArray base64Data = icon.toUtf8().left(4967296);
           icon_image.loadFromData(QByteArray::fromBase64(base64Data), "PNG");
       }

       QImage image_image;
       { //image
           QByteArray base64Data = image.toUtf8().left(4967296);
           image_image.loadFromData(QByteArray::fromBase64(base64Data), "PNG");
       }

       gamesStore.push_back(gameStore(id.toInt(), name, v, desc,"",icon_image,image_image));
   }
}

void games_manager::checkUpdates()
{
    some_updates_available = false;

    for(uint32_t i = 0; i < gamesLocal.size(); i++)
    {
        for(uint32_t j = 0; j < gamesStore.size(); j++)
        {
            uint8_t compare_versions = QString::compare(gamesStore[j].version , gamesLocal[i].version, Qt::CaseInsensitive);

            if(gamesStore[j].id == gamesLocal[i].id  && compare_versions != 0)
            {
                gamesLocal[i].update_available = true;
                some_updates_available = true;
            }
        }
    }
}
