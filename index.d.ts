declare module "react-native-image-crop-picker" {
    export interface Options {
        cropping?: boolean;
        width?: number;
        height?: number;
        multiple?: boolean;
        isRecord?: boolean;//is take video ?
        path?: string;
        includeBase64?: boolean;
        includeExif?: boolean;
        cropperTintColor?: string;
        cropperCircleOverlay?: boolean;
        maxFiles?: number;
        waitAnimationEnd?: boolean;
        smartAlbums?: string[];
        useFrontCamera?: boolean;
        compressVideoPreset?: string;
        compressImageMaxWidth?: number; //also for the video thumbanil
        compressImageMaxHeight?: number;
        compressImageQuality?: number;
        loadingLabelText?: string;
        mediaType: 'photo' | 'video';
        showsSelectedCount?: boolean;
        showCropGuidelines?: boolean;
        hideBottomControls?: boolean;
        enableRotationGesture?: boolean;
    }

    export interface PickData {
        path: string;
        videoThumbnail: string;
        videoDuration: string;
        modificationDate: string;
        size: number;
        data: null | string;
        width: number;
        height: number;
        mime: string;
        exif: null | object;
        cropRect: null | CropRect
    }

    export interface CropRect {
        x: number;
        y: number;
        width: number;
        height: number;
    }

    export function openPicker(options: Options): Promise<PickData | PickData[]>;
    export function openCamera(options: Options): Promise<PickData | PickData[]>;
    export function openCropper(options: Options): Promise<PickData>;
    export function clean(): Promise<void>;
    export function cleanSingle(path: string): Promise<void>;

    export interface ImageCropPicker {
        openPicker(options: Options): Promise<PickData | PickData[]>;
        openCamera(options: Options): Promise<PickData | PickData[]>;
        openCropper(options: Options): Promise<PickData>;
        convertHEIC(options: Options): Promise<PickData>;
        clean(): Promise<void>;
        cleanSingle(path: string): Promise<void>;
    }

    const ImageCropPicker: ImageCropPicker;

    export default ImageCropPicker;
}
