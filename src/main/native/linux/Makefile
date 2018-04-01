TARGET		:= JGecko.so

DESTINATION	:= ../../resources/net/gudenau/jgecko/natives/linux/amd64/

JAVA_DIR	:= /opt/jdk-10
BLD_DIR		:= build
SRC_DIR		:= source
INC_DIRS	:= ../headers $(JAVA_DIR)/include $(JAVA_DIR)/include/linux

C_FILES		:= $(wildcard $(SRC_DIR)/*.c)
O_FILES		:= $(patsubst $(SRC_DIR)/%.c,$(BLD_DIR)/%.o,$(C_FILES))

CC			:= clang
LD			:= clang

WFLAGS		:= -Wall -Werror
CFLAGS		:= -c -fPIE $(addprefix -I,$(INC_DIRS)) $(WFLAGS)
LDFLAGS		:= -shared $(WFLAGS)

all: $(DESTINATION) $(BLD_DIR) $(TARGET)
	cp $(TARGET) $(DESTINATION)/$(TARGET)

$(DESTINATION):
	mkdir -p $(DESTINATION)

$(BLD_DIR):
	mkdir -p $@

$(TARGET): $(O_FILES)
	@echo $@
	@$(LD) $(LDFLAGS) -o $@ $^

$(BLD_DIR)/%.o: $(SRC_DIR)/%.c
	@echo $@
	@$(CC) $(CFLAGS) -o $@ $^
