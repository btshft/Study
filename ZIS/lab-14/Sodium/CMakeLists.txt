cmake_minimum_required(VERSION 3.8)
project(SodiumLab)

set(CMAKE_CXX_STANDARD 14)

find_library(SODIUM sodium /usr/local/Cellar/libsodium/1.0.15/lib)

if(NOT SODIUM)
    message(FATAL_ERROR "sodium library not found")
endif()

set(SOURCE_FILES main.cpp)
add_executable(SodiumLab ${SOURCE_FILES})

target_link_libraries(SodiumLab sodium)