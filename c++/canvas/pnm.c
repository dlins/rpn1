#include "pnm.h"

int save_pnm(const char *name, unsigned char *buf, int x, int y, int depth){
    FILE *fp;

    if ((fp = fopen(name, "w+")) == 0) {
        perror("open");
        exit(1);
    }

    if (depth == 3) {
        fprintf(fp, "P6\n%d %d\n255\n", x, y);
    }

    if (depth == 1) {
        fprintf(fp, "P5\n%d %d\n255\n", x, y);
    }

    fwrite((unsigned char *)buf, x * y * depth, 1, fp);
    fclose(fp);
    return 0;
}

