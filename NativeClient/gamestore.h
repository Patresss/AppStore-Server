#ifndef GAMESTORE_H
#define GAMESTORE_H

#include <QString>
#include <QImage>

class gameStore
{
public:
    int id;
    QString name;
    QString version;
    QString description;
    QString game_path;
    QImage icon;
    QImage img;

    gameStore(int id_init, QString name_init, QString version_init, QString description_init,
              QString game_path_init, QImage icon_init, QImage img_init);
    void install();
};

#endif // GAMESTORE_H
