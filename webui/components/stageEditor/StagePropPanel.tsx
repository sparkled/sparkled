'use client'

import { StagePropType, StagePropTypeValues, StagePropViewModel } from '@/src/types/viewModels'
import { stagePropTypeLabel } from '@/utils/labels'
import {
  Button,
  Card,
  FieldError,
  Input,
  Label,
  NumberField,
  Switch,
  TextField,
} from '@heroui/react'

export type StagePropChanges = Partial<
  Pick<
    StagePropViewModel,
    | 'name'
    | 'code'
    | 'positionX'
    | 'positionY'
    | 'scaleX'
    | 'scaleY'
    | 'rotation'
    | 'ledCount'
    | 'brightness'
    | 'reverse'
    | 'groupCode'
    | 'groupDisplayOrder'
  >
>

export type StagePropPanelProps = {
  stageProp: StagePropViewModel | null
  onChange: (id: string, changes: StagePropChanges) => void
  onDelete: (id: string) => void
  onAdd: (type: StagePropType) => void
}

export function StagePropPanel({ stageProp, onChange, onDelete, onAdd }: StagePropPanelProps) {
  return (
    <Card className='bg-surface/95 absolute top-4 right-4 flex max-h-[calc(100%-2rem)] w-80 flex-col gap-4 overflow-y-auto p-4 shadow-lg backdrop-blur'>
      <Card.Header>
        <Card.Title>{stageProp ? stageProp.name || 'Stage prop' : 'Stage prop'}</Card.Title>
        <Card.Description>
          {stageProp
            ? stagePropTypeLabel[stageProp.type]
            : 'Select a stage prop on the canvas to edit its properties.'}
        </Card.Description>
      </Card.Header>

      {stageProp && (
        <Card.Content className='flex flex-col gap-4'>
          <TextField
            isRequired
            name='name'
            value={stageProp.name}
            onChange={name => onChange(stageProp.id, { name })}
          >
            <Label>Name</Label>
            <Input placeholder='Stage prop name' />
            <FieldError />
          </TextField>

          <TextField
            isRequired
            name='code'
            value={stageProp.code}
            onChange={code => onChange(stageProp.id, { code })}
          >
            <Label>Code</Label>
            <Input placeholder='Unique code' />
            <FieldError />
          </TextField>

          <div className='grid grid-cols-2 gap-3'>
            <NumberField
              name='positionX'
              value={stageProp.positionX}
              onChange={positionX => onChange(stageProp.id, { positionX })}
            >
              <Label>Position X</Label>
              <NumberField.Group>
                <NumberField.Input />
              </NumberField.Group>
            </NumberField>

            <NumberField
              name='positionY'
              value={stageProp.positionY}
              onChange={positionY => onChange(stageProp.id, { positionY })}
            >
              <Label>Position Y</Label>
              <NumberField.Group>
                <NumberField.Input />
              </NumberField.Group>
            </NumberField>
          </div>

          <div className='grid grid-cols-2 gap-3'>
            <NumberField
              formatOptions={{ maximumFractionDigits: 2 }}
              minValue={0.1}
              name='scaleX'
              step={0.1}
              value={stageProp.scaleX}
              onChange={scaleX => onChange(stageProp.id, { scaleX })}
            >
              <Label>Scale X</Label>
              <NumberField.Group>
                <NumberField.Input />
              </NumberField.Group>
            </NumberField>

            <NumberField
              formatOptions={{ maximumFractionDigits: 2 }}
              minValue={0.1}
              name='scaleY'
              step={0.1}
              value={stageProp.scaleY}
              onChange={scaleY => onChange(stageProp.id, { scaleY })}
            >
              <Label>Scale Y</Label>
              <NumberField.Group>
                <NumberField.Input />
              </NumberField.Group>
            </NumberField>
          </div>

          <NumberField
            maxValue={359}
            minValue={0}
            name='rotation'
            value={stageProp.rotation}
            onChange={rotation => onChange(stageProp.id, { rotation })}
          >
            <Label>Rotation (degrees)</Label>
            <NumberField.Group>
              <NumberField.DecrementButton />
              <NumberField.Input />
              <NumberField.IncrementButton />
            </NumberField.Group>
          </NumberField>

          <NumberField
            minValue={0}
            name='ledCount'
            value={stageProp.ledCount}
            onChange={ledCount => onChange(stageProp.id, { ledCount })}
          >
            <Label>LED count</Label>
            <NumberField.Group>
              <NumberField.DecrementButton />
              <NumberField.Input />
              <NumberField.IncrementButton />
            </NumberField.Group>
          </NumberField>

          <NumberField
            maxValue={255}
            minValue={0}
            name='brightness'
            value={stageProp.brightness}
            onChange={brightness => onChange(stageProp.id, { brightness })}
          >
            <Label>Brightness</Label>
            <NumberField.Group>
              <NumberField.DecrementButton />
              <NumberField.Input />
              <NumberField.IncrementButton />
            </NumberField.Group>
          </NumberField>

          <Switch
            isSelected={stageProp.reverse}
            onChange={reverse => onChange(stageProp.id, { reverse })}
          >
            <Switch.Control>
              <Switch.Thumb />
            </Switch.Control>
            <Label>Reverse LED order</Label>
          </Switch>

          <TextField
            name='groupCode'
            value={stageProp.groupCode ?? ''}
            onChange={value => onChange(stageProp.id, { groupCode: value || undefined })}
          >
            <Label>Group code</Label>
            <Input placeholder='Optional group code' />
          </TextField>

          {stageProp.groupCode && (
            <NumberField
              minValue={0}
              name='groupDisplayOrder'
              value={stageProp.groupDisplayOrder ?? 0}
              onChange={groupDisplayOrder => onChange(stageProp.id, { groupDisplayOrder })}
            >
              <Label>Group order</Label>
              <NumberField.Group>
                <NumberField.DecrementButton />
                <NumberField.Input />
                <NumberField.IncrementButton />
              </NumberField.Group>
            </NumberField>
          )}

          <Button variant='danger' onPress={() => onDelete(stageProp.id)}>
            Delete stage prop
          </Button>
        </Card.Content>
      )}

      <Card.Footer className='flex flex-col items-stretch gap-2'>
        <span className='text-muted text-xs font-medium'>Add stage prop</span>
        <div className='grid grid-cols-2 gap-2'>
          {StagePropTypeValues.map(type => (
            <Button key={type} size='sm' variant='secondary' onPress={() => onAdd(type)}>
              {stagePropTypeLabel[type]}
            </Button>
          ))}
        </div>
      </Card.Footer>
    </Card>
  )
}
