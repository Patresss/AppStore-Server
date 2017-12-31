#ifndef GAMES_MANAGER_H
#define GAMES_MANAGER_H

#include "gameLocal.h"
#include "gameStore.h"

class games_manager
{
public:
    int gameStore_SelectedId;
    int gameLocal_SelectedId;
    std::vector<gameStore> gamesStore;
    std::vector<gameLocal> gamesLocal;

    games_manager();
    void run();
    void run_from_store();
    bool install();
    void remove();
    void update();
    void saveLocalInfo();
    void readLocalInfo();
    void getGamesfromRest();

private:
    bool removeDir(const QString & dirName);
};

#endif // GAMES_MANAGER_H
