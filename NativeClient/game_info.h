#ifndef GAME_INFO_H
#define GAME_INFO_H

#include <QString>

class game_info{
public:
  int id;
  QString name;
  QString version;
  QString description;
  bool isInstalled;
  QString game_path;
  QString icon_path;


  game_info(int id_init, QString name_init, QString version_init, QString description_init);
};


#endif // GAME_INFO_H
