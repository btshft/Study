cmake_minimum_required(VERSION 3.8)
project(Crypto)

set(CMAKE_CXX_STANDARD 14)

find_library(CRYPTOPP cryptopp /usr/local/Cellar/cryptopp/5.6.5/lib)

if(NOT CRYPTOPP)
    message(FATAL_ERROR "cryptopp library not found")
endif()

set(SOURCE_FILES main.cpp)
add_executable(Crypto ${SOURCE_FILES})

target_link_libraries(Crypto cryptopp)