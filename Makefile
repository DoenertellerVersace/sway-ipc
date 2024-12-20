# Define the compiler
CC = gcc

# Define the source and header files
SRC = c/sway_ipc.c
HEADER_DIR = /home/jakob/.sdkman/candidates/java/current/include
OS_HEADER_DIR = $(HEADER_DIR)/linux
JNI_HEADER_DIR = /home/jakob/dev/mariaos/sway-ipc/build/generated/sources/headers/java/main

# Define the output directory and target
OUTPUT_DIR = build/libs
TARGET = $(OUTPUT_DIR)/libswayipc.so

# Define the compiler flags
CFLAGS = -I$(HEADER_DIR) -I$(JNI_HEADER_DIR) -I$(OS_HEADER_DIR) -fPIC
LDFLAGS = -shared

# Ensure the output directory exists
mkdir:
	mkdir -p $(OUTPUT_DIR)

# Build the target
build: mkdir $(SRC)
	echo $(HEADER_DIR) $(OS_HEADER_DIR) $(JNI_HEADER_DIR)
	$(CC) $(CFLAGS) $(LDFLAGS) -o $(TARGET) $(SRC)

# Clean the build
clean:
	rm -rf $(OUTPUT_DIR)

# Phony targets
.PHONY: clean