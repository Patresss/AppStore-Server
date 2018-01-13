#include <QProcess>
#include "gameLocal.h"

gameLocal::gameLocal(int id_init, QString name_init, QString version_init, QString description_init,
          QString game_path_init, QString icon_path_init, QString img_path_init)
{
    id = id_init;
    name = name_init;
    version = version_init;
    description = description_init;
    game_path = game_path_init;
    icon_path = icon_path_init;
    img_path = img_path_init;

    update_available = false;
}

void gameLocal::run()
{
    // execute cmd commmand to run game (in background -> non blocking app)
    QString qstr = (QString)"START /B NESemulator\\nestopia.exe " + game_path;
    const char *c = qstr.toLatin1().data();
    system(c);
}

void gameLocal::update()
{

}
