#include "Dump_Fl_Widget.h"

int Dump_Fl_Widget::save_png(const char *filename, int width, int height, unsigned char *rgb, unsigned char *alpha){
    FILE *outfile = fopen(filename, "wb");
    if (outfile == NULL) return DUMP_ERROR;

    png_structp png_ptr;
    png_infop info_ptr;
    png_bytep row_ptr;

    int i;

    png_ptr = png_create_write_struct(PNG_LIBPNG_VER_STRING, 
                                      (png_voidp) NULL, 
                                      (png_error_ptr) NULL, 
                                      (png_error_ptr) NULL);

    if (!png_ptr) return DUMP_ERROR;
  
    info_ptr = png_create_info_struct(png_ptr);
    if (!info_ptr)
    {
        png_destroy_write_struct(&png_ptr, (png_infopp) NULL);
        return DUMP_ERROR;
    }
  
    png_init_io(png_ptr, outfile);

    if (alpha == NULL){
	png_set_IHDR(png_ptr, info_ptr, width, height, 8, PNG_COLOR_TYPE_RGB,
		     PNG_INTERLACE_NONE, PNG_COMPRESSION_TYPE_DEFAULT,
		     PNG_FILTER_TYPE_DEFAULT);
	
	png_write_info(png_ptr, info_ptr);
	
	for (i = 0; i < height; i++) 
	{
	    row_ptr = rgb + 3 * i * width;
	    png_write_rows(png_ptr, &row_ptr, 1);
	}
    }
    else {
	int irgb = 0;
	int irgba = 0;

	int area = width * height;
	unsigned char *rgba = (unsigned char *)malloc(4 * area);

	png_set_IHDR(png_ptr, info_ptr, width, height, 8, 
		     PNG_COLOR_TYPE_RGB_ALPHA,
		     PNG_INTERLACE_NONE, PNG_COMPRESSION_TYPE_DEFAULT,
		     PNG_FILTER_TYPE_DEFAULT);
	
	png_write_info(png_ptr, info_ptr);
	
	for (i = 0; i < area; i++){
            rgba[irgba++] = rgb[irgb++];
            rgba[irgba++] = rgb[irgb++];
            rgba[irgba++] = rgb[irgb++];
            rgba[irgba++] = alpha[i];
	}
	
	for (i = 0; i < height; i++){
            row_ptr = rgba + 4 * i * width;
            png_write_rows(png_ptr, &row_ptr, 1);
	}

	free(rgba);
    }

    png_write_end(png_ptr, info_ptr);
    png_destroy_write_struct(&png_ptr, &info_ptr);

    fclose(outfile);

    return DUMP_OK;
}

int Dump_Fl_Widget::save_eps(const char *filename, int w, int h, unsigned char *rgb){
    int d = 3, count = 1;

    // BEGIN EPS //
    FILE *out = fopen(filename, "w");
    fprintf(out, "%%!PS-Adobe-3.0 EPSF-3.0\n");
    fprintf(out, "%%%%Creator: Raster2EPS\n");
    fprintf(out, "%%%%Title: %s\n", filename);
    fprintf(out, "%%%%BoundingBox: 0 0 %d %d\n", w, h);
    fprintf(out, "%%%%LanguageLevel: 2\n");
    fprintf(out, "%%%%Pages: 1\n");
    fprintf(out, "%%%%DocumentData: Clean7Bit\n\n");

    fprintf(out, "%d %d scale\n", w, h);
    fprintf(out, "%d %d 8 [%d 0 0 %d 0 %d]\n", w, h, w, -h, h);
    fprintf(out, "{currentfile 3 %d mul string readhexstring pop} bind\n", w);
    fprintf(out, "false 3 colorimage\n");

    uchar r, g, b;
    int pos = 0;
    for (int y = 0; y < h; y++){                               // X loop
        for (int x = 0; x < w; x++){                           // Y loop
            long index = (y*w*d) + (x*d); // X/Y -> buf index  
            switch (count) {
                case 1: {                                            // bitmap
                    //const char *buf = buffer[0];
                    switch (d) {
                        case 1: {                                    // 8bit
                            r = g = b = *(rgb+index);
                            break;
                        }
                        case 3:                                      // 24bit
                            r = *(rgb+index+0);
                            g = *(rgb+index+1);
                            b = *(rgb+index+2);
                            break;
                        default:                                     // ??
                            printf("Not supported: chans=%d\n", d);
                            return DUMP_ERROR;
                        }
                    break;
                }
                default:                                             // ?? pixmap, bit vals
                    printf("Not supported: count=%d\n", count);
                    return DUMP_ERROR;
            }

            fprintf(out, "%02x%02x%02x", r, g, b);
            pos = pos + 6;
            if (pos >= 72){
                pos = 0;
                fprintf(out, "\n");
            }
        }
    }

    fprintf(out, "\n%%%%EOF\n");

    fclose(out);
    // END EPS //

    return DUMP_OK;
}

int Dump_Fl_Widget::save(Fl_Widget *widget, const char *name, int type){
    if (widget == 0) return DUMP_ERROR;

    // Draw to a buffer
    uchar *rgb = (uchar*)malloc(widget->w()*widget->h()*3*sizeof(uchar));
    uchar *alpha = 0;

    int x = widget->x(), y = widget->y();
    if (widget->parent() == NULL) x = y = 0;

    fl_read_image(rgb, x, y, widget->w(), widget->h());

//    Fl_Offscreen off = fl_create_offscreen(widget->w(), widget->h());

//    fl_begin_offscreen(off);
//        widget->draw();
//        //fl_read_image(rgb, widget->x(), widget->y(), widget->w(), widget->h());
//        //fl_read_image(rgb, x, y, widget->w(), widget->h());
//    fl_end_offscreen();

    // Save as...
    int info;

    if (type == DUMP_AS_EPS){
        info = save_eps(name, widget->w(), widget->h(), rgb);
    }
    else if (type == DUMP_AS_PNG){
        info = save_png(name, widget->w(), widget->h(), rgb, alpha);
    }
    else DUMP_ERROR;

    // Release the buffer
//    fl_delete_offscreen(off);
    free(rgb);

    return info;
}

