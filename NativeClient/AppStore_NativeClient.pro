#-------------------------------------------------
#
# Project created by QtCreator 2017-11-05T14:37:13
#
#-------------------------------------------------

QT       += core gui
QT       += core websockets

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = AppStore_NativeClient
CONFIG   += console
CONFIG   -= app_bundle

TEMPLATE = app

# The following define makes your compiler emit warnings if you use
# any feature of Qt which has been marked as deprecated (the exact warnings
# depend on your compiler). Please consult the documentation of the
# deprecated API in order to know how to port your code away from it.
#DEFINES += QT_DEPRECATED_WARNINGS

# You can also make your code fail to compile if you use deprecated APIs.
# In order to do so, uncomment the following line.
# You can also select to disable deprecated APIs only up to a certain version of Qt.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0


SOURCES += \
        main.cpp \
        mainwindow.cpp \
    restclient_src/connection.cc \
    restclient_src/helpers.cc \
    restclient_src/restclient.cc \
    game_info.cpp

HEADERS += \
        mainwindow.h \
    restclient_inc/connection.h \
    restclient_inc/helpers.h \
    restclient_inc/restclient.h \
    game_info.h


#INCLUDEPATH +=G:\qt\AppStoreNativeClient\curl-7.56.1-win64-mingw\include
#INCLUDEPATH +=G:\qt\AppStoreNativeClient\curl-7.56.1-win64-mingw\lib
#LIBS += G:\qt\AppStoreNativeClient\curl-7.56.1-win64-mingw\lib\libcurl.dll.a
#LIBS += G:\qt\AppStoreNativeClient\curl-7.56.1-win64-mingw\lib\libcurl.a

INCLUDEPATH +=G:\qt\AppStoreNativeClient\curl-7.56.1-win32-mingw\include
INCLUDEPATH +=G:\qt\AppStoreNativeClient\curl-7.56.1-win32-mingw\lib
LIBS += G:\qt\AppStoreNativeClient\curl-7.56.1-win32-mingw\lib\libcurl.dll.a
LIBS += G:\qt\AppStoreNativeClient\curl-7.56.1-win32-mingw\lib\libcurl.a

#INCLUDEPATH +=G:\qt\AppStoreNativeClient\curl-7.56.1\lib
#LIBS += G:\qt\AppStoreNativeClient\curl-7.56.1\lib\libcurl.dll.a
#LIBS += G:\qt\AppStoreNativeClient\curl-7.56.1\lib\libcurl.a


FORMS += \
        mainwindow.ui
